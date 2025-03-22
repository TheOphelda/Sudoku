public class Main {
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        
        try {
            // Chargement de la grille
            if (args.length > 0) {
                System.out.println("Chargement de la grille depuis le fichier : " + args[0]);
                sudoku.loadGridFromFile(args[0]);
            } else {
                System.out.println("Aucun fichier spécifié. Veuillez saisir la grille manuellement.");
                sudoku.loadGridFromUserInput();
            }
            
            // Validation de la grille initiale
            if (!sudoku.validateGrid()) {
                System.out.println("La grille initiale n'est pas valide !");
                return;
            }
            
            // Affichage de la grille initiale
            System.out.println("\nGrille initiale :");
            sudoku.displayGrid();
            
            // Résolution de la grille
            System.out.println("\nRésolution en cours...");
            if (sudoku.solve()) {
                System.out.println("\nGrille résolue :");
                sudoku.displayGrid();
            } else {
                System.out.println("\nAucune solution n'a été trouvée pour cette grille !");
            }
            
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
