import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main3 {

    public static void main(String[] args) {
        // Get user input for process sizes
        int[] processes = getUserInput("Enter the process sizes (comma-separated):");

        // Get user input for block sizes
        int[] memoryBlocks = getUserInput("Enter the memory block sizes (comma-separated):");

        // Perform allocation
        int[] allocation = bestFitAllocation(memoryBlocks, processes);

        // Create the UI to display results
        SwingUtilities.invokeLater(() -> showResultsUI(memoryBlocks, processes, allocation));
    }

    public static int[] bestFitAllocation(int[] memoryBlocks, int[] processes) {
        // To track the allocation status of each process
        int[] allocation = new int[processes.length];
        for (int i = 0; i < allocation.length; i++) {
            allocation[i] = -1; // Initialize all allocations as not allocated
        }

        // Iterate through each process
        for (int i = 0; i < processes.length; i++) {
            int bestBlockIndex = -1;

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

    public static void showResultsUI(int[] memoryBlocks, int[] processes, int[] allocation) {
        // Create the main frame
        JFrame frame = new JFrame("Best Fit Memory Allocation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Main panel with a layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Process Allocation Table
        String[] processColumns = {"Process Number.", "Process Size (KB)", "Block Size (KB)", "Allocated Block", "Remaining Space in Block (KB)"};
        String[][] processData = new String[processes.length][5];

        // Temporary copy of original memory blocks to calculate remaining space
        int[] originalMemoryBlocks = new int[memoryBlocks.length];
        System.arraycopy(memoryBlocks, 0, originalMemoryBlocks, 0, memoryBlocks.length);

        for (int i = 0; i < processes.length; i++) {
            processData[i][0] = "Process " + (i + 1);
            processData[i][1] = processes[i] + " KB";
            processData[i][2] = memoryBlocks[i] + " KB"; // Display current memory block size

            if (allocation[i] != -1) {
                int blockIndex = allocation[i];
                processData[i][3] = "Block " + (blockIndex + 1);
                processData[i][4] = memoryBlocks[blockIndex] + " KB"; // Remaining space in the allocated block
            } else {
                processData[i][3] = "Not Allocated";
                processData[i][4] = "-"; // No remaining space
            }
        }

        JTable processTable = new JTable(processData, processColumns);
        processTable.setPreferredScrollableViewportSize(new Dimension(800, 300));
        processTable.setFillsViewportHeight(true);

        // Make the memory block column editable
        processTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));

        // Custom Cell Renderer for Coloring
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Apply custom coloring
                if (column == 3) { // Allocated Block column
                    if ("Not Allocated".equals(value)) {
                        cell.setBackground(Color.RED);
                        cell.setForeground(Color.WHITE);
                    } else {
                        cell.setBackground(Color.GREEN);
                        cell.setForeground(Color.BLACK);
                    }
                } else if (column == 4) { // Remaining Space column
                    cell.setBackground(Color.LIGHT_GRAY);
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                }

                return cell;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < processColumns.length; i++) {
            processTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane processScrollPane = new JScrollPane(processTable);

        // Add components to panel
        panel.add(new JLabel("Process Allocation Results:"), BorderLayout.NORTH);
        panel.add(processScrollPane, BorderLayout.CENTER);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to get comma-separated input from the user
    public static int[] getUserInput(String prompt) {
        String input = JOptionPane.showInputDialog(prompt);
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input cannot be empty!");
            System.exit(1);
        }

        String[] inputArray = input.split(",");
        List<Integer> values = new ArrayList<>();

        // Parse the input values
        for (String s : inputArray) {
            try {
                values.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number format!");
                System.exit(1);
            }
        }

        // Convert List to Array
        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }

        return result;
    }
}
