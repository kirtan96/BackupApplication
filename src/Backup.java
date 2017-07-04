import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kirtan on 7/2/17.
 */
public class Backup {

    private static List<WatchEvent<?>> events, destEvents;
    private static Path sourceDir, destinationDir;
    private static WatchService watcher;
    private static WatchKey watckKey;
    private static WatchService destWatcher;
    private static WatchKey destWatckKey;
    private static Timer timer;
    static final String BACKUP = "Start Backup";
    private static JButton backupButton;
    private static JFrame frame;

    public static void main(String[] args) {

//        try {
//            watcher = sourceDir.getFileSystem().newWatchService();
//            sourceDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
//                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
//
//            watckKey = watcher.take();
//
//            events = watckKey.pollEvents();
//
//            destWatcher = sourceDir.getFileSystem().newWatchService();
//            destinationDir.register(destWatcher, StandardWatchEventKinds.ENTRY_CREATE,
//                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
//
//            destWatckKey = destWatcher.take();
//
//            destEvents = destWatckKey.pollEvents();
//
//
//        } catch (Exception e) {
//            System.out.println("Error: " + e.toString());
//        }
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JLabel label1 = new JLabel("Source Directory: ");
        JLabel srcDir = new JLabel();
        JButton srcButton = new JButton("Select");
        JLabel label2 = new JLabel("Destination Directory: ");
        JLabel destDir = new JLabel();
        JButton destButton = new JButton("Select");
        backupButton = new JButton(BACKUP);
        JPanel srcPanel = new JPanel();
        srcPanel.setLayout(new FlowLayout());
        srcPanel.add(label1);
        srcPanel.add(srcDir);
        srcPanel.add(srcButton);
        JPanel destPanel = new JPanel();
        destPanel.setLayout(new FlowLayout());
        destPanel.add(label2);
        destPanel.add(destDir);
        destPanel.add(destButton);
        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout());
        panel3.add(backupButton);
        frame.add(srcPanel, BorderLayout.NORTH);
        frame.add(destPanel, BorderLayout.CENTER);
        frame.add(panel3, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setSize(500,150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        srcButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                switch (chooser.showOpenDialog(frame)) {
                    case JFileChooser.APPROVE_OPTION:
                        sourceDir = chooser.getSelectedFile().toPath();
                        srcDir.setText(sourceDir.toString());
                        break;
                }
            }
        });

        destButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                switch (chooser.showOpenDialog(frame)) {
                    case JFileChooser.APPROVE_OPTION:
                        destinationDir = chooser.getSelectedFile().toPath();
                        destDir.setText(destinationDir.toString());
                        break;
                }
            }
        });

        backupButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(backupButton.getText().equals(BACKUP)){
                    if(sourceDir != null && destinationDir != null){
                        backupButton.setText("Stop Backup");
                        srcButton.setEnabled(false);
                        destButton.setEnabled(false);
                        timer = new Timer();
                        timer.schedule(new BackupThread(), 0, 5000);
                    }
                    else{
                        JOptionPane.showMessageDialog(frame,
                                "Select both: Source Directory and Destination Directory",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    srcButton.setEnabled(true);
                    destButton.setEnabled(true);
                    timer.cancel();
                    timer.purge();
                    backupButton.setText(BACKUP);
                }
            }
        });


    }

    static class BackupThread extends TimerTask {

        @Override
        public void run(){
//            for (WatchEvent event : events) {
//                System.out.println(event.kind());
//                Path sourceFilePath = sourceDir.resolve((Path) event.context());
//                String fileName = sourceFilePath.toFile().getName();
//                Path destDir = Paths.get("/Users/Kirtan/Desktop/Backup/" + fileName);
//                System.out.println(sourceFilePath.toUri().toString());
//                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
//
//                    System.out.println("Creating: " + event.context().toString());
//                    try {
//                        Files.copy(sourceFilePath, destDir);
//                        System.out.println("Backup Successful");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
////                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
////                    System.out.println("Delete: " + event.context().toString());
////                }
////                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
////                    System.out.println("Modify: " + event.context().toString());
////                }
//            }
//            try {
//                watcher = sourceDir.getFileSystem().newWatchService();
//                sourceDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
//                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
//                watckKey = watcher.take();
//                events = watckKey.pollEvents();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            for(File file: sourceDir.toFile().listFiles()){
                Path destDir = Paths.get(destinationDir + "/" + file.getName());
                if(!destDir.toFile().exists()){
                    try {
                        Files.copy(file.toPath(), destDir);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame,
                                "Cannot find Source/Destination Directory",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        backupButton.doClick();
                        return;
                    }
                }
            }
        }
    }
}
