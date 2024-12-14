public class Main {

    public static void main(String[] args) {
        // Memory blocks and process sizes
        int[] memoryBlocks = {200, 300, 500}; // Memory block sizes in KB
        int[] processes = {150, 350, 250};   // Process sizes in KB

        // Perform allocation
        int[] allocation = bestFitAllocation(memoryBlocks, processes);

        // Display allocation results
        System.out.println("Process Allocation Results:");
        for (int i = 0; i < processes.length; i++) {
            if (allocation[i] != -1) {
                System.out.println("Process " + (i + 1) + " (" + processes[i] + " KB) -> Block " + (allocation[i] + 1));
            } else {
                System.out.println("Process " + (i + 1) + " (" + processes[i] + " KB) -> Not Allocated");
            }
        }

        // Display remaining memory
        System.out.println("\nRemaining Memory in Blocks:");
        for (int i = 0; i < memoryBlocks.length; i++) {
            System.out.println("Block " + (i + 1) + ": " + memoryBlocks[i] + " KB");
        }
    }

    public static int[] bestFitAllocation(int[] memoryBlocks, int[] processes) {
        // To track the allocation status of each process
        int[] allocation = new int[processes.length];
        for (int i = 0; i < allocation.length; i++) {
            allocation[i] = -1; // Initialize all allocations as not allocated
        }

        for (int i = 0; i < processes.length; i++) {
            int bestBlockIndex = -1; //to track the best fitting block for each process.

            // Find the best fit block for the current process
            for (int j = 0; j < memoryBlocks.length; j++) {
                if (memoryBlocks[j] >= processes[i]) {
                    if (bestBlockIndex == -1 || memoryBlocks[j] < memoryBlocks[bestBlockIndex]) {
                        bestBlockIndex = j;
                    }
                }
            }

            // Allocate the process to the best block, if found
            if (bestBlockIndex != -1) {
                allocation[i] = bestBlockIndex;
                memoryBlocks[bestBlockIndex] -= processes[i]; // Reduce available size of the block
            }
        }

        return allocation;
    }
}
