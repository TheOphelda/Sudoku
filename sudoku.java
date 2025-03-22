import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Sudoku {
    private int[][] grid;
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    
    public Sudoku() {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
    }
    
    // Chargement de la grille depuis un fichier
    public void loadGridFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < GRID_SIZE) {
                String[] values = line.trim().split("\\s+");
                if (values.length != GRID_SIZE) {
                    throw new IOException("Format de ligne invalide : chaque ligne doit contenir 9 chiffres");
                }
                for (int col = 0; col < GRID_SIZE; col++) {
                    int value = Integer.parseInt(values[col]);
                    if (value < 0 || value > 9) {
                        throw new IOException("Valeur invalide : les chiffres doivent être entre 0 et 9");
                    }
                    grid[row][col] = value;
                }
                row++;
            }
            if (row != GRID_SIZE) {
                throw new IOException("Nombre de lignes incorrect : la grille doit avoir 9 lignes");
            }
        }
    }
    
    // Saisie manuelle de la grille
    public void loadGridFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez saisir la grille ligne par ligne (9 chiffres séparés par des espaces) :");
        
        for (int row = 0; row < GRID_SIZE; row++) {
            System.out.printf("Ligne %d : ", row + 1);
            String[] values = scanner.nextLine().trim().split("\\s+");
            
            while (values.length != GRID_SIZE) {
                System.out.println("Erreur : veuillez entrer exactement 9 chiffres séparés par des espaces");
                System.out.printf("Ligne %d : ", row + 1);
                values = scanner.nextLine().trim().split("\\s+");
            }
            
            for (int col = 0; col < GRID_SIZE; col++) {
                try {
                    int value = Integer.parseInt(values[col]);
                    if (value < 0 || value > 9) {
                        throw new NumberFormatException();
                    }
                    grid[row][col] = value;
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : veuillez entrer des chiffres entre 0 et 9");
                    col = -1; // Recommencer la ligne
                    System.out.printf("Ligne %d : ", row + 1);
                    values = scanner.nextLine().trim().split("\\s+");
                }
            }
        }
    }
    
    // Affichage de la grille
    public void displayGrid() {
        System.out.println("┌───────┬───────┬───────┐");
        for (int row = 0; row < GRID_SIZE; row++) {
            System.out.print("│ ");
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(grid[row][col] + " ");
                }
                if ((col + 1) % SUBGRID_SIZE == 0 && col < GRID_SIZE - 1) {
                    System.out.print("│ ");
                }
            }
            System.out.println("│");
            
            if ((row + 1) % SUBGRID_SIZE == 0 && row < GRID_SIZE - 1) {
                System.out.println("├───────┼───────┼───────┤");
            }
        }
        System.out.println("└───────┴───────┴───────┘");
    }
    
    // Validation d'un nombre dans une position donnée
    private boolean isValid(int row, int col, int num) {
        // Vérification de la ligne
        for (int x = 0; x < GRID_SIZE; x++) {
            if (grid[row][x] == num) {
                return false;
            }
        }
        
        // Vérification de la colonne
        for (int x = 0; x < GRID_SIZE; x++) {
            if (grid[x][col] == num) {
                return false;
            }
        }
        
        // Vérification de la sous-grille 3x3
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // Résolution de la grille (algorithme de backtracking)
    public boolean solve() {
        int row = -1;
        int col = -1;
        boolean isEmpty = false;
        
        // Trouve la première case vide
        for (int i = 0; i < GRID_SIZE && !isEmpty; i++) {
            for (int j = 0; j < GRID_SIZE && !isEmpty; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = true;
                }
            }
        }
        
        // Si aucune case vide n'est trouvée, la grille est résolue
        if (!isEmpty) {
            return true;
        }
        
        // Essaie les chiffres de 1 à 9
        for (int num = 1; num <= GRID_SIZE; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;
                if (solve()) {
                    return true;
                }
                grid[row][col] = 0; // Backtrack
            }
        }
        
        return false;
    }
    
    // Validation de la grille complète
    public boolean validateGrid() {
        // Vérifie que toutes les valeurs sont entre 0 et 9
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] < 0 || grid[row][col] > 9) {
                    return false;
                }
            }
        }
        
        // Vérifie que les valeurs non nulles respectent les règles du Sudoku
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != 0) {
                    int temp = grid[row][col];
                    grid[row][col] = 0;
                    if (!isValid(row, col, temp)) {
                        grid[row][col] = temp;
                        return false;
                    }
                    grid[row][col] = temp;
                }
            }
        }
        
        return true;
    }
}
