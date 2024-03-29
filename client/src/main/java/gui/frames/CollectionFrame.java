package gui.frames;

import collectionitems.MusicBand;
import collectionitems.WrongArgumentException;
import connection.MusicBandConnection;
import connection.MusicBandResponse;
import connection.ResponseStatus;
import console.input.EndOfInputException;
import console.util.scriptexecution.ReadingScriptFileException;
import console.util.scriptexecution.ScriptExecutor;
import gui.collectiontable.DescriptionRowFilter;
import gui.collectiontable.MultiLineTableCellRenderer;
import gui.collectiontable.MusicBandTableModel;
import gui.components.MusicBandLabel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionFrame extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel middlePanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private JPanel bottomMainPanel = new JPanel();
    private JButton helpButton = new JButton();
    private JLabel userLabel = new JLabel();
    private JTable collectionTable;
    private JButton executeScriptButton = new JButton();
    private JButton infoButton = new JButton();
    private JButton insertAtButton = new JButton();
    private JButton addIfMinButton = new JButton();
    private JButton countLesserButton = new JButton();
    private JButton addIfMaxButton = new JButton();
    private JButton clearButton = new JButton();
    private JButton addButton = new JButton();
    private JButton deleteButton = new JButton();
    private JButton editButton = new JButton();
    private JButton filterDescriptionButton = new JButton();

    private final MusicBandConnection connection;
    private List<MusicBand> bands;

    private List<MusicBandLabel> bandsLabels = new ArrayList<>();
    private MusicBandTableModel tableModel;

    public CollectionFrame(String username, MusicBandConnection connection) throws IOException, ClassNotFoundException, InterruptedException {
        Color mainColor = new Color(88, 119, 235);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        this.setBounds(width/2 - 600, height/2 - 400, 1200, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        //main holder
        add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(mainColor);

        //header
        topPanel.setPreferredSize(new Dimension(1200, 90));
        topPanel.setBackground(mainColor);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.weightx = 1180;
        headerConstraints.weighty = 96;
        headerConstraints.gridx = 0;
        headerConstraints.gridy = 0;
        headerConstraints.gridheight = 3;
        headerConstraints.gridwidth = 1;
        topPanel.add(new JLabel(), headerConstraints);
        headerConstraints.gridx = 1;
        headerConstraints.gridy = 0;
        headerConstraints.gridheight = 1;
        headerConstraints.gridwidth = 1;
        topPanel.add(new JLabel(), headerConstraints);
        helpButton.setText("Help");
        helpButton.setPreferredSize(new Dimension(273, 30));
        headerConstraints.gridx = 1;
        headerConstraints.gridy = 1;
        headerConstraints.gridheight = 1;
        headerConstraints.gridwidth = 1;
        topPanel.add(helpButton, headerConstraints);
        headerConstraints.gridx = 1;
        headerConstraints.gridy = 2;
        topPanel.add(new JLabel(), headerConstraints);
        headerConstraints.gridx = 2;
        headerConstraints.gridy = 0;
        topPanel.add(new JLabel(), headerConstraints);
        headerConstraints.gridx = 2;
        headerConstraints.gridy = 1;
        headerConstraints.gridx = 2;
        headerConstraints.gridy = 2;
        topPanel.add(new JLabel(), headerConstraints);
        headerConstraints.gridx = 3;
        headerConstraints.gridy = 0;
        headerConstraints.gridheight = 3;
        headerConstraints.gridwidth = 1;
        userLabel.setText(username);
        ImageIcon userIcon = new ImageIcon(getClass().getResource("user.png"));
        userLabel.setIcon(userIcon);
        userLabel.setHorizontalTextPosition(JLabel.LEFT);
        userLabel.setFont(new Font("OPPO Sans", Font.ITALIC, 35));
        userLabel.setForeground(Color.WHITE);
        topPanel.add(userLabel, headerConstraints);
        topPanel.revalidate();

        middlePanel.setPreferredSize(new Dimension(1180, 450));
        JPanel leftMargin = new JPanel();
        leftMargin.setBackground(mainColor);
        leftMargin.setPreferredSize(new Dimension(10,450));
        JPanel rightMargin = new JPanel();
        rightMargin.setBackground(mainColor);
        leftMargin.setPreferredSize(new Dimension(10,450));
        mainPanel.add(leftMargin, BorderLayout.EAST);
        mainPanel.add(rightMargin, BorderLayout.WEST);
        mainPanel.add(middlePanel);

        bottomPanel.setPreferredSize(new Dimension(1200, 260));
        bottomPanel.setBackground(mainColor);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        bottomPanel.setLayout(new BorderLayout());
        JPanel leftMarginBottom = new JPanel();
        leftMarginBottom.setBackground(mainColor);
        leftMarginBottom.setPreferredSize(new Dimension(10, 260));
        bottomPanel.add(leftMarginBottom, BorderLayout.WEST);
        JPanel topMarginBottom = new JPanel();
        topMarginBottom.setBackground(mainColor);
        bottomPanel.add(topMarginBottom, BorderLayout.NORTH);
        bottomPanel.add(bottomMainPanel);
        //table 10 rows 18 cols
        //collection table 8 rows 12 cols
        bottomMainPanel.setLayout(new GridBagLayout());
        bottomMainPanel.setBackground(mainColor);
        GridBagConstraints bottomConstraints = new GridBagConstraints();
        JPanel bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setPreferredSize(new Dimension(370, 96));
        bottomButtonsPanel.setBackground(mainColor);
        bottomConstraints.gridx = 12;
        bottomConstraints.gridy = 0;
        bottomConstraints.gridheight = 4;
        bottomConstraints.gridwidth = 6;
        bottomMainPanel.add(bottomButtonsPanel, bottomConstraints);

        Dimension buttonsSize = new Dimension(110, 27);
        Font buttonsFont = new Font("Arial", Font.PLAIN, 9);
        executeScriptButton.setText("ExecuteScript");
        executeScriptButton.setFont(buttonsFont);
        executeScriptButton.setPreferredSize(buttonsSize);
        bottomButtonsPanel.add(executeScriptButton);
        infoButton.setText("Info");
        infoButton.setPreferredSize(buttonsSize);
        infoButton.setFont(buttonsFont);
        bottomButtonsPanel.add(infoButton);
        insertAtButton.setText("InsertAt");
        insertAtButton.setPreferredSize(buttonsSize);
        insertAtButton.setFont(buttonsFont);
        bottomButtonsPanel.add(insertAtButton);
        addIfMinButton.setText("AddIfMin");
        addIfMinButton.setPreferredSize(buttonsSize);
        addIfMinButton.setFont(buttonsFont);
        bottomButtonsPanel.add(addIfMinButton);
        clearButton.setText("Clear");
        clearButton.setPreferredSize(buttonsSize);
        clearButton.setFont(buttonsFont);
        bottomButtonsPanel.add(clearButton);
        countLesserButton.setText("CountLesserGenre");
        countLesserButton.setPreferredSize(buttonsSize);
        countLesserButton.setFont(buttonsFont);
        bottomButtonsPanel.add(countLesserButton);
        addIfMaxButton.setText("AddIfMax");
        addIfMaxButton.setPreferredSize(buttonsSize);
        addIfMaxButton.setFont(buttonsFont);
        bottomButtonsPanel.add(addIfMaxButton);

        bottomConstraints.gridx = 0;
        bottomConstraints.gridy = 9;
        bottomConstraints.gridheight = 1;
        bottomConstraints.gridwidth = 3;
        JPanel emptyBottomPanel = new JPanel();
        emptyBottomPanel.setBackground(mainColor);
        emptyBottomPanel.setPreferredSize(new Dimension(300, 50));
        bottomMainPanel.add(emptyBottomPanel, bottomConstraints);

        bottomConstraints.gridx = 5;
        bottomConstraints.gridy = 9;
        bottomConstraints.gridheight = 1;
        bottomConstraints.gridwidth = 6;
        JPanel underTableButtonsPanel = new JPanel();
        underTableButtonsPanel.setBackground(mainColor);
        bottomMainPanel.add(underTableButtonsPanel, bottomConstraints);

        addButton.setText("Add");
        deleteButton.setText("Delete");
        editButton.setText("Edit");
        filterDescriptionButton.setText("FilterDescription");
        underTableButtonsPanel.add(addButton);
        underTableButtonsPanel.add(deleteButton);
        underTableButtonsPanel.add(editButton);
        underTableButtonsPanel.add(filterDescriptionButton);

        //functionality
        this.connection = connection;
        bands = connection.sendCommand("load").musicBandList;
        tableModel = new MusicBandTableModel(bands);
        collectionTable = new JTable(tableModel);
        TableRowSorter tableRowSorter = new TableRowSorter(tableModel);
        collectionTable.setRowSorter(tableRowSorter);
        MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();
        collectionTable.setDefaultRenderer(String[].class, renderer);
        collectionTable.setRowHeight(75);

        bottomConstraints.gridx = 0;
        bottomConstraints.gridy = 0;
        bottomConstraints.gridheight = 8;
        bottomConstraints.gridwidth = 12;
        JScrollPane tableScrollablePane = new JScrollPane(collectionTable);
        tableScrollablePane.setPreferredSize(new Dimension(795, 180));
        bottomMainPanel.add(tableScrollablePane, bottomConstraints);

        helpButton.addActionListener( e -> JOptionPane.showMessageDialog(null, "Use this application to add, edit and delete MusicBand objects in the database"));
        infoButton.addActionListener( e -> {
            try {
                JOptionPane.showMessageDialog(null, connection.sendCommand("info").response);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ConnectionLost");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        executeScriptButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try {
                    ScriptExecutor.executeScript(connection, selectedFile);
                    JOptionPane.showMessageDialog(null, "ScriptExecutedSuccessfully");
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "ScriptFileNotFound");
                } catch (ReadingScriptFileException ex) {
                    JOptionPane.showMessageDialog(null, "CouldNotReadScriptFile");
                } catch (EndOfInputException ex) {
                    JOptionPane.showMessageDialog(null, "EndOfInput");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        filterDescriptionButton.addActionListener(e -> {
            String descriptionStart = JOptionPane.showInputDialog(this, "DescriptionStarts");
            tableRowSorter.setRowFilter(new DescriptionRowFilter(descriptionStart));
        });
        deleteButton.addActionListener(e -> {
            int row = collectionTable.getSelectedRow();
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                MusicBandResponse response = connection.sendCommand("remove_by_id", "" + id);
                if(response.status == ResponseStatus.FAIL){
                    JOptionPane.showMessageDialog(null, response.response);
                }
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "NoConnection");
                System.exit(0);
            }
        });

        connection.setUpdater(this::update);

        addButton.addActionListener(e -> new BandFrame("Add New Band", connection, BandFormType.ADD).setVisible(true));
        editButton.addActionListener(e -> {
            if(collectionTable.getSelectedRow() == -1){
                JOptionPane.showMessageDialog(null, "Select band");
            }
            else {
                int selectedId = (Integer) collectionTable.getValueAt(collectionTable.getSelectedRow(), 0);
                MusicBand selectedBand = bands.stream().filter(b -> b.getId() == selectedId).findFirst().get();
                BandFrame frame = new BandFrame("Edit Selected Band", connection, BandFormType.EDIT);
                frame.setBandValues(selectedBand);
                frame.setVisible(true);
            }
        });
        insertAtButton.addActionListener(e -> new BandFrame("Add New Band", connection, BandFormType.INSERT_AT).setVisible(true));
        addIfMaxButton.addActionListener(e ->  new BandFrame("Add New Band", connection, BandFormType.ADD_IF_MAX).setVisible(true));
        addIfMinButton.addActionListener(e ->  new BandFrame("Add New Band", connection, BandFormType.ADD_IF_MIN).setVisible(true));
        clearButton.addActionListener(e -> {
            try {
                connection.sendCommand("clear");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ConnectionLost");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        countLesserButton.addActionListener(e -> new CountLesserGenreFrame("Count Lesser Genre", connection).setVisible(true));

        middlePanel.setLayout(null);
        for(MusicBand b: bands){
            System.out.println(middlePanel.getSize());
            MusicBandLabel label = new MusicBandLabel(b);
            middlePanel.add(label);
            bandsLabels.add(label);
        }
    }

    private void update(MusicBandResponse updateResponse){
        if(updateResponse.status == ResponseStatus.UPDATE_DELETE){
            for (int id: updateResponse.ids){
                for(int i = 0; i < bands.size(); i++){
                    if(bands.get(i).getId() == id){
                        bands.remove(i);
                        middlePanel.remove(bandsLabels.get(i));
                        middlePanel.revalidate();
                        middlePanel.repaint();
                        bandsLabels.remove(i);
                        break;
                    }
                }
            }
        }
        if(updateResponse.status == ResponseStatus.UPDATE_CLEAR){
            String username = updateResponse.response;
            bands.removeIf(b -> b.getOwnerUsername().equals(username));
            bandsLabels.removeIf(l -> {
                if(l.getOwnerUsername().equals(username)){
                    middlePanel.remove(l);
                    return true;
                }
                else {
                    return false;
                }
            });

            middlePanel.revalidate();
            middlePanel.repaint();
        }
        if(updateResponse.status == ResponseStatus.UPDATE_ADD){
            for (MusicBand band: updateResponse.musicBandList){
                bands.add(band);
                MusicBandLabel label = new MusicBandLabel(band);
                middlePanel.add(label);
                bandsLabels.add(label);
            }
            middlePanel.revalidate();
            middlePanel.repaint();
        }
        if(updateResponse.status == ResponseStatus.UPDATE_UPDATE){
            MusicBand updated = updateResponse.musicBandList.get(0);
            for(MusicBand b: bands){
                if(b.getId() == updated.getId()){
                    try {
                        b.setName(updated.getName());
                        b.setCoordinates(updated.getCoordinates());
                        b.setCreationDate(updated.getCreationDate());
                        b.setNumberOfParticipants(updated.getNumberOfParticipants());
                        b.setAlbumsCount(updated.getAlbumsCount());
                        b.setDescription(updated.getDescription());
                        b.setGenre(updated.getGenre());
                        b.setBestAlbum(updated.getBestAlbum());
                    } catch (WrongArgumentException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
            bandsLabels.removeIf(l -> {
                if(l.getId() == updated.getId()){
                    middlePanel.remove(l);
                    return true;
                }
                return false;
            });
            MusicBandLabel label = new MusicBandLabel(updated);
            bandsLabels.add(label);
            middlePanel.add(label);
        }
        tableModel.fireTableDataChanged();
        middlePanel.revalidate();
        middlePanel.repaint();
    }
}