package com.kinsey.passwords.items;

/**
 * Created by Yvonne on 5/2/2017.
 */

public class MyDataObject {
//    private SectionsPagerAdapter mSectionsPagerAdapter;
    Account account = new Account();
    int selectedPos = -1;
    int sortOrder = -1;
//    private AccountListActivityFragment fragList;
//    private AccountPlaceholderFrag1 frag1;
//    private AccountPlaceholderFrag2 frag2;
//    private AccountPlaceholderFrag3 frag3;
//    private int fragListPos = -1;
//    private int frag1Pos = -1;
//    private int frag2Pos = -1;
//    private int frag3Pos = -1;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
