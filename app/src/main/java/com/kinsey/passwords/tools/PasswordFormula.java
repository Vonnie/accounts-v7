package com.kinsey.passwords.tools;

import android.util.Log;

import java.util.Locale;
import java.util.Random;

/**
 * Created by Yvonne on 2/26/2017.
 */

public class PasswordFormula {
    private static final String TAG = "PasswordFormula";

    String[] keybrd1 = { "A", "S", "D", "F", "G"};
    String[] keybrd2 = { "Q", "W", "E", "R", "T", "C", "V"};
    String[] keybrd3 = { "H", "J", "K", "L" };
    String[] keybrd4 = { "Y", "U", "I", "O", "P", "B", "N", "M"};
    String[] keyLbrd = { "A", "S", "D", "F", "G",
            "Q", "W", "E", "R", "T", "Z", "X", "C", "V"};
    String[] keyRbrd = { "H", "J", "K", "L", "Y",
            "U", "I", "O", "P", "B", "N", "M"};
    String[] symbols = {"!", "(", ")", "?", "[", "]", "_", "`", "~",
            ";", ":", "!", "#", "$", "%", "^", "&", "*", "+", "="};

    private boolean rightHand = false;

    final Random myRandom = new Random();

    public String createPassword(int passwordLen) {
        String aLetter = findPrimaryLetter();
        String password = aLetter.toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        int iSize = 0;
        int randLtr = 0;
        if (rightHand) {
            iSize = keyRbrd.length;
            randLtr = myRandom.nextInt(iSize - 1);
            aLetter = keyRbrd[randLtr];
        } else {
            iSize = keyLbrd.length;
            randLtr = myRandom.nextInt(iSize - 1);
            aLetter = keyLbrd[randLtr];
        }
//		Log.v(TAG, "password2Prime: " + aLetter);
        password = password + aLetter.toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
//		Log.i(TAG, "password start " + password);
        if (rightHand) {
            iSize = keyRbrd.length;
            randLtr = myRandom.nextInt(iSize - 1);
            aLetter = keyRbrd[randLtr];
        } else {
            iSize = keyLbrd.length;
            randLtr = myRandom.nextInt(iSize - 1);
            aLetter = keyLbrd[randLtr];
        }
        password = password + aLetter.toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
        password = password + getAdjacentLetter(aLetter).toLowerCase(Locale.ENGLISH);
//		Log.i(TAG, "password start " + password);

        int cap = myRandom.nextInt(passwordLen - 1);
//		Log.v(TAG, "password: " + password + ":" + password.length() + ":" + cap);
//		Log.v(TAG, "passwordCap: " + password.substring(0, cap));
//		Log.v(TAG, "passwordCap: " + password.substring(cap, cap+1).toUpperCase(Locale.ENGLISH));
        if (cap == password.length() - 1) {
            password = password.substring(0, cap)
                    + password.substring(cap, cap + 1).toUpperCase(Locale.ENGLISH);
        } else {
//			Log.v(TAG, "passwordCap: " + password.substring(cap+1));
            password = password.substring(0, cap)
                    + password.substring(cap, cap + 1).toUpperCase(Locale.ENGLISH)
                    + password.substring(cap + 1);
        }
//		r.nextInt(max - min + 1) + min;
        int num = 0;

        num = myRandom.nextInt(9 - 1 + 1) + 1;

//		int numPlace = myRandom.nextInt(password.length()-1);
        double dblPlace = Math.random();
        if (passwordLen == 10) {
        } else {
            dblPlace = .8 * dblPlace;
        }

        if (dblPlace < 0.1) {
            password = String.valueOf(num) + password.substring(0);
        } else if (dblPlace < 0.2) {
            password = password.substring(0, 1) + String.valueOf(num) + password.substring(1);
        } else if (dblPlace < 0.3) {
            password = password.substring(0, 2) + String.valueOf(num) + password.substring(2);
        } else if (dblPlace < 0.4) {
            password = password.substring(0, 3) + String.valueOf(num) + password.substring(3);
        } else if (dblPlace < 0.5) {
            password = password.substring(0, 4) + String.valueOf(num) + password.substring(4);
        } else if (dblPlace < 0.6) {
            password = password.substring(0, 5) + String.valueOf(num) + password.substring(5);
        } else if (dblPlace < 0.7) {
            password = password.substring(0, 6) + String.valueOf(num) + password.substring(6);
        } else if (dblPlace < 0.8) {
            password = password.substring(0, 7) + String.valueOf(num) + password.substring(7);
        } else if (dblPlace < 0.9) {
//			Log.i(TAG, "pswd8 " + password.substring(0,8));
            password = password.substring(0, 8) + String.valueOf(num) + password.substring(8);
        } else {
            dblPlace = Math.random();
            if (dblPlace < 0.3) {
                password = password.substring(0, 9) + String.valueOf(num) + password.substring(9);
            } else if (dblPlace < 0.6) {
                password = password.substring(0, 10) + String.valueOf(num) + password.substring(10);
            } else {
                password = password.substring(0) + String.valueOf(num);
            }
        }
//		password = password.substring(0, numPlace)
//				+ num
//				+ password.substring(numPlace);
//		return password.substring(0,8);
        if (passwordLen == 10) {
            return insertSymbol(password, 9);
        } else {

            return insertSymbol(password, 7);
        }
    }

    private String insertSymbol(String password, int maxSub) {
        String randSym = symbols[myRandom.nextInt(symbols.length-1)];
        int randSymPos = myRandom.nextInt(maxSub);
//        Log.d(TAG, "randSym: " + randSym);
//        Log.d(TAG, "randSymPos: " + randSymPos);
//        Log.d(TAG, "password: " + password);
        String newPassword = password.substring(0, randSymPos) +
                randSym;
//        Log.d(TAG, "password: " + newPassword);

        if (randSymPos < maxSub) {
//            Log.d(TAG, "password: " + password.substring(randSymPos, maxSub));
            newPassword += password.substring(randSymPos, maxSub);
//            Log.d(TAG, "password: " + newPassword);
        }
        return newPassword;
    }

    private String findPrimaryLetter() {
        String aLetter = "";
        int iSize = 0;
        int randLtr = 0;
        int randPrimaryLtr = myRandom.nextInt(3);
        switch (randPrimaryLtr) {
            case 0: {
                iSize = keybrd1.length;
                randLtr = myRandom.nextInt(iSize-1);
                aLetter = keybrd1[randLtr];
                rightHand = false;
                break;
            }
            case 1: {
                iSize = keybrd2.length;
                randLtr = myRandom.nextInt(iSize-1);
                aLetter = keybrd2[randLtr];
                rightHand = false;
                break;
            }
            case 2: {
                iSize = keybrd3.length;
                randLtr = myRandom.nextInt(iSize-1);
                aLetter = keybrd3[randLtr];
                rightHand = true;
                break;
            }
            case 3: {
                iSize = keybrd4.length;
                randLtr = myRandom.nextInt(iSize-1);
                aLetter = keybrd4[randLtr];
                rightHand = true;
            }
        }
        return aLetter;
    }

//	private String findAdjLetter(String[] ar) {
//		int iSize = ar.length;
//		int randLtr = myRandom.nextInt(iSize);
//		return ar[randLtr];
//	}

    private String getAdjacentLetter(String letter) {
        String adjacentLetter = "";
        // String[] ar;
        if (letter.compareToIgnoreCase("A") == 0) {
            String[] ar = { "Q", "W", "S", "Z", "A" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("B") == 0) {
            String[] ar = { "V", "G", "B" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("C") == 0) {
            String[] ar = { "D", "X", "V", "C" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("D") == 0) {
            String[] ar = { "F", "E", "C", "S", "D" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("E") == 0) {
            String[] ar = { "D", "W", "R", "S", "E" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("F") == 0) {
            String[] ar = { "D", "G", "R", "V", "F" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("G") == 0) {
            String[] ar = { "F", "T", "B", "V", "G" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("H") == 0) {
            String[] ar = { "J", "Y", "B", "U", "H" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("I") == 0) {
            String[] ar = { "K", "U", "O", "J", "I" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("J") == 0) {
            String[] ar = { "H", "U", "N", "M", "K", "J" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("K") == 0) {
            String[] ar = { "J", "L", "I", "M", "K" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("L") == 0) {
            String[] ar = { "K", "O", "L" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("M") == 0) {
            String[] ar = { "N", "K", "J", "M" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("N") == 0) {
            String[] ar = { "M", "H", "J", "N" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("O") == 0) {
            String[] ar = { "K", "P", "L", "I", "O" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("P") == 0) {
            String[] ar = { "O", "P" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("Q") == 0) {
            String[] ar = { "W", "A", "Q" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("R") == 0) {
            String[] ar = { "E", "T", "F", "R" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("S") == 0) {
            String[] ar = { "A", "X", "W", "D", "S" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("T") == 0) {
            String[] ar = { "R", "G", "F", "Y", "T" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("U") == 0) {
            String[] ar = { "I", "J", "H", "Y", "U" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("V") == 0) {
            String[] ar = { "C", "F", "G", "B", "V" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("W") == 0) {
            String[] ar = { "Q", "E", "S", "D", "W" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("X") == 0) {
            String[] ar = { "Z", "S", "C", "D", "X" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("Y") == 0) {
            String[] ar = { "H", "U", "Y" };
            return findAdjLetter(ar).toLowerCase();
        } else if (letter.compareToIgnoreCase("Z") == 0) {
            String[] ar = { "A", "S", "X", "Z" };
            return findAdjLetter(ar).toLowerCase();
        }
        return adjacentLetter;
    }

    private String findAdjLetter(String[] ar) {
        int iSize = ar.length;
        int randLtr = myRandom.nextInt(iSize-1);
        return ar[randLtr];
    }


}
