package logic;

import collectionitems.MusicBand;
import collectionitems.WrongArgumentException;
import connection.MusicBandRequest;
import connection.MusicBandResponse;
import connection.ResponseStatus;
import data.CollectionManager;
import data.database.QueryExecutionException;
import data.database.users.User;
import data.database.users.UserDao;
import logic.commands.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.List;

public class CommandsExecutor {
    private final CollectionManager collectionManager;
    private final UserDao userDao;
    private final List<User> userList;

    public CommandsExecutor(CollectionManager collectionManager, UserDao userDao) throws QueryExecutionException {
        this.collectionManager = collectionManager;
        this.userDao = userDao;
        this.userList = userDao.getAllUsers();
    }

    public AbstractMap.SimpleEntry<MusicBandResponse, MusicBandResponse> executeCommand(MusicBandRequest command)
            throws IOException, NoSuchAlgorithmException, QueryExecutionException {
        MusicBandResponse response = new MusicBandResponse();
        if(command.name.equals("register")){
            if(userList.stream().filter(u -> u.getUsername().equals(command.username)).findAny().orElse(null) == null){
                User user = new User(command.username, encryptPassword(command.password));
                userDao.addUser(user);
                userList.add(user);
                response.status = ResponseStatus.SUCCESS;
                response.response = "New user successfully registered!";
                return new AbstractMap.SimpleEntry<>(response, null);
            }
            else {
                response.status = ResponseStatus.FAIL;
                response.response = "This username is already used!";
                return new AbstractMap.SimpleEntry<>(response, null);
            }
        }
        if(!checkUser(command.username, command.password)){
            response.status = ResponseStatus.FAIL;
            response.response = "Authorization failed";
            return new AbstractMap.SimpleEntry<>(response, null);
        }
        if(command.name.equals("login")){
            response.status = ResponseStatus.SUCCESS;
            response.response = "Authorization successful";
            return new AbstractMap.SimpleEntry<>(response, null);
        }
        if(command.name.equals("load")){
            response.status = ResponseStatus.SUCCESS;
            response.musicBandList = collectionManager.getAll();
            return new AbstractMap.SimpleEntry<>(response, null);
        }
        Command executableCommand = getCommandObject(command, command.username);
        try {
            String executionResult = executableCommand.execute();
            response.status = ResponseStatus.SUCCESS;
            response.response = executionResult;
            if(executableCommand instanceof ChangingCollectionCommand){
                MusicBandResponse updateResponse = ((ChangingCollectionCommand)executableCommand).getUpdateResponse();
                return new AbstractMap.SimpleEntry<>(response, updateResponse);
            }
            return new AbstractMap.SimpleEntry<>(response, null);
        } catch (WrongArgumentException e) {
            response.status = ResponseStatus.FAIL;
            response.response = e.getMessage();
            return new AbstractMap.SimpleEntry<>(response, null);
        }
    }

    private boolean checkUser(String username, String password) throws NoSuchAlgorithmException {
        String encryptedPassword = encryptPassword(password);
        synchronized (this){
            User user = userList.stream().filter(u -> u.getUsername().equals(username) &&
                    u.getEncryptedPass().equals(encryptedPassword)).findAny().orElse(null);
            return user != null;
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8), 0, password.length());
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }


    private Command getCommandObject(MusicBandRequest command, String username){
        if(command == null){
            return null;
        }
        String commandName = command.name;
        String arg = command.arg;
        MusicBand band = command.band;

        if(commandName.equals("show")){
            return new ShowCommand(collectionManager);
        }
        if(commandName.equals("update")){
            return new UpdateCommand(arg, collectionManager, band, username);
        }
        if(commandName.equals("info")){
            return new InfoCommand(collectionManager);
        }
        if(commandName.equals("add")){
            return new AddCommand(collectionManager, band, username);
        }
        if(commandName.equals("remove_by_id")){
            return new RemoveByIdCommand(collectionManager, arg, username);
        }
        if(commandName.equals("clear")){
            return new ClearCommand(collectionManager, username);
        }
        if(commandName.equals("save")){
            return new SaveCommand(collectionManager);
        }
        if(commandName.equals("insert_at")){
            return new InsertAtCommand(collectionManager, arg, band, username);
        }
        if(commandName.equals("insert_if_max")){
            return new InsertIfMaxCommand(collectionManager, band, username);
        }
        if(commandName.equals("insert_if_min")){
            return new InsertIfMinCommand(collectionManager, band, username);
        }
        if(commandName.equals("count_less_than_genre")){
            return new CountLessThanGenreCommand(collectionManager, arg);
        }
        if(commandName.equals("filter_starts_with_description")){
            return new FilterStartsWithDescriptionCommand(collectionManager, arg);
        }
        if(commandName.equals("print_descending")){
            return new PrintDescendingCommand(collectionManager);
        }
        else{
            return null;
        }
    }
}
