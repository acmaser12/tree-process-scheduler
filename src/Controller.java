/*
written by: Adam Maser
CSC 420
Week 5 Project
Controller.java - Execution class for process-scheduler
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Controller {

    public static void main(String[] args) {
        // start program
        Controller program = new Controller();
        program.start();
    }

    private void start() {
        // output required info
        System.out.println("Submitted by Adam Maser - masera1@csp.edu");
        System.out.println("I certify that this is my own work\n");

        // load data from file
        ProcessInfo[] processes = loadFile();

        // create AVLTree of ProcessInfo Objects
        // creating new AVLTree also sorts the objects into AVLTree (see AVLTree class)
        AVLTree<ProcessInfo> newAVLTree = new AVLTree<>(processes);

        // output AVLTree by level
        printAVLTree(newAVLTree);

        // execute each process
        AVLTree<ProcessInfo> finishedProcesses = executeProcesses(newAVLTree);

        // loop through and print the results of each process
        System.out.println("\n");
        for (ProcessInfo finishedProcess : finishedProcesses) {
            System.out.println(finishedProcess.displayCompletedInfo());
        }

    }

    private ProcessInfo[] loadFile() {
        // create Scanner object
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(new File("processes.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found, exiting program...");
            System.exit(2);
        }
        // create ArrayList for processes (since I don't know how many processes are coming)
        ArrayList<ProcessInfo> processes = new ArrayList<>();

        while (fileReader.hasNextLine()) {
            String[] info = fileReader.nextLine().split("\\|");
            processes.add(new ProcessInfo(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3])));
        }

        // copy to array (since that is the constructor in AVLTree class)
        ProcessInfo[] processArray = new ProcessInfo[processes.size()];

        for (int i = 0; i < processes.size(); i++) {
            processArray[i] = processes.get(i);
        }

        // return ArrayList of Processes
        return processArray;
    }

    private void printAVLTree(AVLTree<ProcessInfo> tree) {
        int levelSize = 1;  // number of elements in level
        int location = 0;  // location in tree
        int currentLevel = 0;  // current level of tree
        Iterator<ProcessInfo> iterator = tree.iterator();

        // iterate through tree and output each process in each level
        while (location < tree.getSize()) {
            System.out.println("Current Level: " + currentLevel);
            for (int i = 0; i < levelSize; i++) {
                // if the location is bigger than tree, break loop
                if (location >= tree.getSize()) {
                    break;
                }
                System.out.println("\t" + iterator.next().toString());
                location++;
            }
            // increment currentLevel and increase level (each subsequent level of a tree is 2x bigger than prev)
            currentLevel++;
            levelSize *= 2;
        }
    }

    private AVLTree<ProcessInfo> executeProcesses(AVLTree<ProcessInfo> tree) {
        // get size of tree
        int originalSize = tree.getSize();

        // create new AVLTree to hold finished processes
        AVLTree<ProcessInfo> completedTree = new AVLTree<>();

        // loop through processes until all are complete
        while (completedTree.size() < originalSize) {
            // get iterator
            Iterator<ProcessInfo> iterator = tree.iterator();
            for (int i = 0; i < tree.getSize(); i++) {
                ProcessInfo currentProcess = iterator.next();
                boolean isCompleted = currentProcess.executeProcess((int)System.currentTimeMillis() % 10000);
                // if completed, add to completedTree and remove from tree
                if (!isCompleted) {
                    completedTree.insert(currentProcess);
                    tree.remove(currentProcess);
                }
            }

        }
        return completedTree;
    }
}
