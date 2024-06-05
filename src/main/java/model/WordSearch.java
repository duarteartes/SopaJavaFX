package model;

import com.example.SopaDeLetras.SopaException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class WordSearch {
    Random random = new Random();
    private final int width = 10;
    private final int lenght = 10;
    private final int maxAllowedWords = 5;
    private char[][] puzzle;

    private ArrayList<int[]> discoveredCells;

    public ArrayList<PuzzelWord> discoverablePuzzelWords;


    public char[][] getPuzzle() {
        return puzzle;
    }

    public WordSearch(ArrayList<String> words) {
        if(words.size() > this.maxAllowedWords)
            return;

        puzzle = new char[width][lenght];
        buildPuzzle(words);
        fillBlanks();
        discoveredCells = new ArrayList<>();
    }

    private void fillBlanks() {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if(this.puzzle[i][j] == '\u0000'){
                    this.puzzle[i][j] = (char) (random.nextInt(26) + 'A');
                }
            }
        }
    }

    private void buildPuzzle(ArrayList<String> words) {

        for (int i = 0; i < words.size(); i++) {
            String originalString = words.get(i);
            String uppercaseString = originalString.toUpperCase();
            words.set(i, uppercaseString);
        }

        discoverablePuzzelWords = new ArrayList<>();
        String firstWordToPlace = words.get(0);

        if (random.nextBoolean()) {
            int coordInitX = (puzzle.length / 2) / (firstWordToPlace.length() / 2);
            int coordInitY = puzzle.length / 2;
            if (coordInitX < 0 || coordInitX + firstWordToPlace.length() > puzzle.length) {
                throw new SopaException("La palabra no cabe en el rompecabezas.");
            }
            placeWord(true, coordInitX - 1, coordInitY - 1, firstWordToPlace);
        } else {
            int coordInitX = puzzle.length / 2;
            int coordInitY = (puzzle.length / 2) / (firstWordToPlace.length() / 2);
            if (coordInitY < 0 || coordInitY + firstWordToPlace.length() > puzzle.length) {
                throw new SopaException("La palabra '" + firstWordToPlace + "' no cabe en el rompecabezas.");
            }
            placeWord(false, coordInitX - 1, coordInitY - 1, firstWordToPlace);
        }

        for (int i = 1; i < words.size(); i++) {
            boolean wordPlaced = false;

            for (PuzzelWord placedWord : discoverablePuzzelWords) {
                if (match(words.get(i), placedWord)) {
                    wordPlaced = true;
                    break;
                }
            }

            if (!wordPlaced) {
                for (int j = 0; j < 10; j++) {
                    int randomRow = random.nextInt(0, puzzle.length);
                    int randomCol = random.nextInt(0, puzzle.length);

                    if (placeWord(random.nextBoolean(), randomRow, randomCol, words.get(i))) {
                        wordPlaced = true;
                        break;
                    }
                }

                if (!wordPlaced) {
                    throw new SopaException("No se puede colocar la palabra " + words.get(i) + " en el rompecabezas.");
                }
            }
        }
    }

    private boolean match(String wordToPlace, PuzzelWord placedWord) {
        System.out.println();
        for (int i = 0; i < wordToPlace.length(); i++) {
            if(placedWord.hasChar(wordToPlace.charAt(i))){
                int[] coords = placedWord.coordsOfMatch(wordToPlace.charAt(i));
                if(coords[0] > -1){
                    if(placedWord.getIndexRowInit() == placedWord.getIndexRowEnd()) {
                        if (placeWord(true, coords[0] - i, coords[1], wordToPlace))
                            return true;
                    }
                    else {
                        if (placeWord(false, coords[0], coords[1] - i, wordToPlace))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean placeWord(boolean vo, int indexRowInit, int indexColumnInit, String wordToPlace) {
        if (wordFits( vo,  indexRowInit,  indexColumnInit,  wordToPlace)){
            int indexColumn = indexColumnInit;
            int indexRow = indexRowInit;
            for (char c:wordToPlace.toCharArray()) {
                this.puzzle[indexRow][indexColumn] = c;
                if (vo) indexRow++;
                else indexColumn++;
            }
            if(vo)
                discoverablePuzzelWords.add(new PuzzelWord(indexRowInit,indexRowInit+wordToPlace.length()-1 ,indexColumnInit,indexColumnInit,wordToPlace));
            else
                discoverablePuzzelWords.add(new PuzzelWord(indexRowInit,indexRowInit,indexColumnInit,indexColumnInit + +wordToPlace.length()-1,wordToPlace));
            return true;
        }else{
            return false;
        }
    }

    private boolean wordFits(boolean vo, int indexRowInit, int indexColumnInit, String wordToPlace) {
        if(indexRowInit < 0 || indexColumnInit < 0 )
            return false;
        if(vo && indexRowInit + wordToPlace.length() > puzzle.length)
            return false;
        if(!vo && indexColumnInit + wordToPlace.length() > puzzle.length)
            return false;
        int indexColumn = indexColumnInit;
        int indexRow = indexRowInit;
        for (char c:wordToPlace.toCharArray()) {
            if (this.puzzle[indexRow][indexColumn] == '\u0000' || this.puzzle[indexRow][indexColumn] == c  ) {
                if (vo) indexRow++;
                else indexColumn++;
            }else{
                return false;
            }
        }
        return true;
    }

    public int  wordsToFind() {
        return discoverablePuzzelWords.size();
    }

    public boolean gameOver() {
        for (PuzzelWord word : discoverablePuzzelWords) {
            if (!word.isDiscovered())
                return false;
        }
        return true;
    }

    public boolean checkWord(String wordToGuess) {
        for (PuzzelWord word : discoverablePuzzelWords) {
            if(word.tryGuess(wordToGuess)){
                if(word.getIndexColumnInit() == word.getIndexColumnEnd()) {
                    for (int i = word.getIndexRowInit(); i <= word.getIndexRowEnd(); i++) {
                        discoveredCells.add(new int[]{i, word.getIndexColumnInit()});
                    }
                }else{
                    for (int i = word.getIndexColumnInit(); i <= word.getIndexColumnEnd(); i++) {
                        discoveredCells.add(new int[]{word.getIndexRowInit(), i});
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isDiscoveredCell(int row, int column) {
        for (int [] cell: discoveredCells) {
            if (row == cell[0] && column == cell[1])
                return true;
        }
        return false;
    }
}
