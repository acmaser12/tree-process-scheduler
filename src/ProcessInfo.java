/*
written by: Adam Maser
CSC420
Week 4 Project
ProcessInfo.java -- stores information for each ProcessInfo object
 */

public class ProcessInfo implements Comparable<ProcessInfo>{
    private String processName;
    private int processId;
    private int processPriority;
    private int processRemainingRuntime;
    private long processStartTime;
    private long processEndTime;
    private long processElapsedTime;
    private int timesExecuted = 0;

    ProcessInfo(String processName, int processId, int processPriority, int processRemainingRuntime) {
        this.processName = processName;
        this.processId = processId;
        this.processPriority = processPriority;
        this.processRemainingRuntime = processRemainingRuntime;
    }

    boolean executeProcess(int currentTime) {
        // if this is the  first execution, set start time
        if (timesExecuted == 0) {
            this.processStartTime = currentTime;
        }
        timesExecuted++;
        // calculate execution time from processPriority
        int executionTime = 10 - this.processPriority;

        // simulate execution using Thread.sleep
        try {
            Thread.sleep(executionTime);
        } catch (InterruptedException ex) {
            System.out.println("Process interrupted, closing program...");
            System.exit(2);
        }

        // decrement remaining time
        this.processRemainingRuntime = this.processRemainingRuntime - executionTime;

        // check if process has finished executing, return false if it has finished
        if (this.processRemainingRuntime <= 0) {
            // calculate end time
            this.processEndTime = currentTime + executionTime;
            // call endProcess method
            endProcess();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compareTo(ProcessInfo process) {
        // compare processPriority
        return Integer.compare(this.processPriority, process.processPriority);
    }

    @Override
    public String toString() {
        return "Process Name: " + this.processName + "\t\tProcess Id: " + this.processId + "\t\tProcess Priority: " +
                this.processPriority + "\t\tProcess Remaining Runtime: " + this.processRemainingRuntime;
    }

    String displayCompletedInfo() {
        return "Process Name: " + this.processName + "\t\tProcess Priority: " + this.processPriority +
                "\t\tCompletion Time: " + this.processElapsedTime;
    }

    private void endProcess() {
        // calculate elapsed time
        this.processElapsedTime = this.processEndTime - this.processStartTime;
    }

}