package com.kinsey.passwords.items;

import com.kinsey.passwords.AccountListActivityFragment;
import com.kinsey.passwords.AccountPlaceholderFrag1;
import com.kinsey.passwords.AccountPlaceholderFrag2;
import com.kinsey.passwords.AccountPlaceholderFrag3;

/**
 * Created by Yvonne on 5/2/2017.
 */

public class MyDataObject {
    Account account = new Account();
    private AccountListActivityFragment fragList;
    private AccountPlaceholderFrag1 frag1;
    private AccountPlaceholderFrag2 frag2;
    private AccountPlaceholderFrag3 frag3;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AccountPlaceholderFrag1 getFrag1() {
        return frag1;
    }

    public void setFrag1(AccountPlaceholderFrag1 frag1) {
        this.frag1 = frag1;
    }

    public AccountPlaceholderFrag2 getFrag2() {
        return frag2;
    }

    public void setFrag2(AccountPlaceholderFrag2 frag2) {
        this.frag2 = frag2;
    }

    public AccountPlaceholderFrag3 getFrag3() {
        return frag3;
    }

    public void setFrag3(AccountPlaceholderFrag3 frag3) {
        this.frag3 = frag3;
    }

    public AccountListActivityFragment getFragList() {
        return fragList;
    }

    public void setFragList(AccountListActivityFragment fragList) {
        this.fragList = fragList;
    }
}
