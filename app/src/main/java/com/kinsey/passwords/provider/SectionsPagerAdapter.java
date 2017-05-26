package com.kinsey.passwords.provider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvonne on 4/27/2017.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";
    private static final boolean DEBUG = false;

    static final int NUM_ITEMS = 3;
    //    boolean mTwoPane = false;
    private int fragListPos = -1;
    private int frag1Pos = 0;
    private int frag2Pos = 1;
    private int frag3Pos = 2;
    //    private Account account = new Account();
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    //    private MyDataObject myDataObject;
    //    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
//    private Fragment mCurrentFragment = null;
    private List<Fragment> mFragments;
    //    private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
//    private AccountPlaceholderFrag1 frag1 = new AccountPlaceholderFrag1();
//    private AccountPlaceholderFrag2 frag2 = new AccountPlaceholderFrag2();
//    private AccountPlaceholderFrag3 frag3 = new AccountPlaceholderFrag3();
    private List<Fragment> fragments = new ArrayList<Fragment>();
//    private AccountListActivityFragment fragList;
//    private AccountPlaceholderFrag1 frag1;
//    private AccountPlaceholderFrag2 frag2;
//    private AccountPlaceholderFrag3 frag3;
//
//    private int frag1RowId = -1;
//    private int frag2RowId = -1;
//    private int frag3RowId = -1;

//        final Context mContext;
//        final WeakReference<Fragment>[] m_fragments;
//        private SparseArray<WeakReference<Fragment>> mFragments = new SparseArray<>();
//        List<AccountPlaceholderFragment> listFrags = new ArrayList<AccountPlaceholderFragment>();

    public SectionsPagerAdapter(FragmentManager fragmentManager,
                                int fragListPos, int frag1Pos, int frag2Pos, int frag3Pos,
                                List<Fragment> fragments) {

//                                AccountListActivityFragment fragList,
//                                AccountPlaceholderFrag1 frag1,
//                                AccountPlaceholderFrag2 frag2,
//                                AccountPlaceholderFrag3 frag3) {
//                                MyDataObject myDataObject) {
//                              boolean twopane,   ) {
        super(fragmentManager);
        this.mFragmentManager = fragmentManager;
//        this.myDataObject = myDataObject;
//        Log.d(TAG, "SectionsPagerAdapter: myDataObject set");
////        mFragments = fragments;
//        this.account = myDataObject.getAccount();
//        this.mTwoPane = twopane;
//        if (mTwoPane) {
//            fragListPos = -1;
//            frag1Pos = 0;
//            frag2Pos = 1;
//            frag3Pos = 2;
//        } else {
//            fragListPos = 0;
//            frag1Pos = 1;
//            frag2Pos = 2;
//            frag3Pos = 3;
//        }
        this.fragments = fragments;
        this.fragListPos = fragListPos;
        this.frag1Pos = frag1Pos;
        this.frag2Pos = frag2Pos;
        this.frag3Pos = frag3Pos;
//        this.fragList = fragList;
//        this.frag1 = frag1;
//        this.frag2 = frag2;
//        this.frag3 = frag3;

//        Log.d(TAG, "SectionsPagerAdapter: twopane " + this.mTwoPane);
//        Log.d(TAG, "SectionsPagerAdapter: twopane " + this.mTwoPane);
//        frag1 = AccountPlaceholderFrag1.newInstance(0);
//        frag2 = AccountPlaceholderFrag2.newInstance(1);
//        frag3 = AccountPlaceholderFrag3.newInstance(2);
//        Fragment frag = AccountPlaceholderFrag1.newInstance(0);
//        fragments.put(0, frag);
//        frag = AccountPlaceholderFrag2.newInstance(1);
//        fragments.put(1, frag);
//        frag = AccountPlaceholderFrag3.newInstance(2);
//        fragments.put(2, frag);
//        Log.d(TAG, "SectionsPagerAdapter: size " + fragments.size());
    }
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
//        switch (position) {
//            case 0:
//                frag1 = (AccountPlaceholderFrag1) createdFragment;
//                break;
//            case 1:
//                frag2 = (AccountPlaceholderFrag2) createdFragment;
//                break;
//            case 2:
//                frag3 = (AccountPlaceholderFrag3) createdFragment;
//                break;
//
//        }
//        return createdFragment;
//
////            Fragment f = (Fragment) super.instantiateItem(container, position);
////            mFragments.put(position, new WeakReference<>(f));  // Remember what fragment was in position
////            return f;
//
//
//
////            if (mCurTransaction == null) {
////                mCurTransaction = mFragmentManager.beginTransaction();
////            }
////
////            final long itemId = getItemId(position);
////
////            // Do we already have this fragment?
////            String name = makeFragmentName(container.getId(), itemId);
////            Fragment fragment = mFragmentManager.findFragmentByTag(name);
////            if (fragment != null) {
////                if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
////                mCurTransaction.attach(fragment);
////            } else {
////                fragment = getItem(position);
////                if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
////                mCurTransaction.add(container.getId(), fragment,
////                        makeFragmentName(container.getId(), itemId));
////            }
////            if (fragment != mCurrentPrimaryItem) {
////                fragment.setMenuVisibility(false);
////                fragment.setUserVisibleHint(false);
////            }
////
////            return fragment;
//
//    }


    @Override
    public int getCount() {
//        return mFragments.size();
        if (fragListPos == -1) {
            return 3;
        } else {
            return 4;
        }
    }

//    @Override
//    public Fragment getItem(int position) {
//        return mFragments.get(position);
//    };

    //    @Override
//    public Fragment getItem(int position) {
//        Fragment frag = mFragments.get(position);
//        switch (position) {
//            case 0:
//                if (frag instanceof AccountPlaceholderFrag1)
//                    AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1)frag;
//                return frag1.newInstance(position);
//            case 1:
//                AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2)frag;
//                return frag2.newInstance(position);
//            case 2:
//                AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3)frag;
//                return frag3.newInstance(position);
//
//        }
//        return null;
//    };


//    @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
////            AccountPlaceholderFragment frag = AccountPlaceholderFragment.newInstance(position + 1);
////            listFrags.add(frag);
////            Log.d(TAG, "getItemUpdates: " + frag.getSection());
////            return frag;
////            return AccountPlaceholderFragment.newInstance(position + 1);
//
//        Fragment frag = null;
//
//        Log.d(TAG, "getItem: pos " + position);
//        switch (position) {
//            case 0:
//                frag = AccountPlaceholderFrag1.newInstance(position);
//                Log.d(TAG, "getItem: frag frag1 ");
////                frag1 = (AccountPlaceholderFrag1)frag;
////                frag = fragments.get("0");
//                break;
//            case 1:
//                frag = AccountPlaceholderFrag2.newInstance(position);
////                frag2 = (AccountPlaceholderFrag2)frag;
////                frag = fragments.get("1");
//                break;
//            case 2:
//                frag = AccountPlaceholderFrag3.newInstance(position);
////                frag3 = (AccountPlaceholderFrag3)frag;
////                frag = fragments.get("2");
//                break;
//        }
//
//        if (frag != null) {
//            fragments.put(position, frag);
//        }
//
//        return frag;
//    }

//    @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
////            AccountPlaceholderFragment frag = AccountPlaceholderFragment.newInstance(position + 1);
////            listFrags.add(frag);
////            Log.d(TAG, "getItemUpdates: " + frag.getSection());
////            return frag;
////            return AccountPlaceholderFragment.newInstance(position + 1);
//
////        Fragment frag = null;
//
//        Log.d(TAG, "getItem: pos " + position);
//        switch (position) {
//            case 0:
//                frag1 = AccountPlaceholderFrag1.newInstance(position, this.account.getId());
////                frag1.setAccount(account);
////                frag1 = AccountPlaceholderFrag1.newInstance(position, account.getId());
////                frag1 = new AccountPlaceholderFrag1();
////                Bundle arguments = new Bundle();
////                arguments.putInt(AccountPlaceholderFrag1.ARG_SECTION_NUMBER, 0);
//
//                return frag1;
////                mFragments.add(frag1);
////                Log.d(TAG, "getItem: frag frag1 ");
////                frag1 = (AccountPlaceholderFrag1)frag;
////                frag = fragments.get("0");
////                break;
//            case 1:
//                frag2 = AccountPlaceholderFrag2.newInstance(position, this.account.getId());
//                return frag2;
////                frag2 = (AccountPlaceholderFrag2)frag;
////                frag = fragments.get("1");
////                break;
//            case 2:
//                frag3 = AccountPlaceholderFrag3.newInstance(position, this.account.getId());
//                return frag3;
////                frag3 = (AccountPlaceholderFrag3)frag;
////                frag = fragments.get("2");
////                break;
//        }

//        if (frag != null) {
//            fragments.put(position, frag);
//        }
//
//        return frag;

//        return null;
//    }
//        frag1 = AccountPlaceholderFrag1.newInstance(0);
//        frag2 = AccountPlaceholderFrag2.newInstance(1);
//        frag3 = AccountPlaceholderFrag3.newInstance(2);

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: position " + position);
//        if (position == fragListPos) {
//            AccountListActivityFragment fragList = AccountListActivityFragment.newInstance(2);
//            return fragList;
//        } else {
//            return fragments.get(position);
//        }
        return fragments.get(position);

//        int limit = fragments.size();
//        for (int i = 0; i < limit; i++) {
//            switch (position) {
//
//            }
//          if (fragment)
//        }

//        if (position == fragListPos) {
////            fragList = AccountListActivityFragment.newInstance();
//            return fragList;
//
//        } else if (position == frag1Pos) {
////            if (frag1RowId == -1) {
////                frag1 = AccountPlaceholderFrag1.newInstance();
////            }
//            return frag1;
//
//        } else if (position == frag2Pos) {
////            if (frag2RowId == -1) {
////                frag2 = AccountPlaceholderFrag2.newInstance(position);
////            }
//            return frag2;
//
//        } else if (position == frag3Pos) {
////            if (frag3RowId == -1) {
////                frag3 = AccountPlaceholderFrag3.newInstance(position);
////            }
//            return frag3;
//        }
//        return null;
    }


//    @Override
//    public void notifyDataSetChanged() {
////        Log.d(TAG, "notifyDataSetChanged: id " + AccountListActivity.account.getId());
//        if (frag1 != null) {
////            if (frag1RowId != -1) {
////            frag1RowId = account.getId();
//            frag1.fillPage();
////            }
//        }
//
////        AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2) fragments.get(1);
//        if (frag2 != null) {
////            if (frag2RowId != -1) {
////            frag2RowId = account.getId();
//            frag2.fillPage();
////            }
//        }
////        AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3) fragments.get(2);
//        if (frag3 != null) {
////            if (frag3RowId != -1) {
////            frag3RowId = account.getId();
//            frag3.fillPage();
////            }
//        }
//        super.notifyDataSetChanged();
//    }

//    public void loadPage(int position) {
//        Log.d(TAG, "loadPage: pos " + position);
//        if (position == myDataObject.getFrag1Pos()) {
//            Log.d(TAG, "loadPage: loadPage1 acct " + myDataObject.getAccount());
//            if (myDataObject.getFrag1() == null) {
//                Log.d(TAG, "loadPage: null frag1");
//            }
////            frag1RowId = account.getId();
//            myDataObject.getFrag1().fillPage(myDataObject.getAccount());
//        } else if (position == myDataObject.getFrag2Pos()) {
//            if (myDataObject.getFrag2() == null) {
//                Log.d(TAG, "loadPage: frag2 is null");
//            }
////            frag2RowId = account.getId();
//            myDataObject.getFrag2().fillPage(myDataObject.getAccount());
//        } else if (position == myDataObject.getFrag3Pos()) {
//            if (myDataObject.getFrag3() == null) {
//                Log.d(TAG, "loadPage: frag3 is null");
//            }
////            frag3RowId = account.getId();
//            myDataObject.getFrag3().fillPage(myDataObject.getAccount());
//        }
//    }

//    @Override
//    public void notifyDataSetChanged() {
//        AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1) fragments.get(0);
//        if (frag1 != null) {
//            frag1.fillPage(this.account);
//        }
//        AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2) fragments.get(1);
//        if (frag2 != null) {
//            frag2.fillPage(this.account);
//        }
//        AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3) fragments.get(2);
//        if (frag3 != null) {
//            frag3.fillPage(this.account);
//        }
//        super.notifyDataSetChanged();
//    }

//    @Override
//    public void notifyDataSetChanged() {
//        AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1)mFragments.get(0);
//        if (frag1 != null) {
//            frag1.fillPage(this.account);
//        }
//        AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2) mFragments.get(1);
//        if (frag2 != null) {
//            frag2.fillPage(this.account);
//        }
//        AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3) mFragments.get(2);
//        if (frag3 != null) {
//            frag3.fillPage(this.account);
//        }
//        super.notifyDataSetChanged();
//    }

//    public boolean validateFrags(int position) {
//        switch (position) {
//            case 0:
//                frag1.validatePage1();
//                break;
//            case 1:
//                frag2.validatePage2();
//                break;
//            case 2:
//                frag3.validatePage3();
//                break;
//        }
//        return true;
//    }

//    public boolean validateFrag1Errors() {
//        return myDataObject.getFrag1().validatePageErrors();
//    }
//
//    public boolean validateFrag2Errors() {
//        return myDataObject.getFrag2().validatePageErrors();
//    }
//
//    public boolean validateFrag3Errors() {
//        return myDataObject.getFrag3().validatePageErrors();
//    }

//    public boolean validateFrags() {
//        AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1) fragments.get(0);
//        if (frag1.validatePage1()) {
//            AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2) fragments.get(1);
//            if (frag2.validatePage2()) {
//                AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3) fragments.get(2);
//                if (frag3.validatePage3()) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
////            Set keys = fragments.keySet();
////            for (Iterator i = keys.iterator(); i.hasNext();) {
////                String key = (String) i.next();
////                if (key.equals('0')) {
////                    AccountPlaceholderFrag1 frag = (AccountPlaceholderFrag1) fragments.get(key);
////                }
////
////            }
//    }

//        public Fragment getFragment(int position) {
//            WeakReference<Fragment> ref = mFragments.get(position);
//            Fragment f = ref != null ? ref.get() : null;
//            if (f == null) {
//                Log.d(TAG, "fragment for " + position + " is null!");
//            }
//            return f;
//        }

//    public void fillPages() {
//
////        if (frag1 != null) {
////            frag1.fillPage();
////        }
////        if (frag2 != null) {
////            frag2.fillPage();
////        }
////        if (frag3 != null) {
////            frag3.fillPage();
////        }
////        for (int i = 0; i < fragments.size(); i++) {
////
////            if (i == frag1Pos) {
////                ((AccountPlaceholderFrag1) fragments.get(i)).fillPage();
////            }
////            if (i == frag2Pos) {
////                ((AccountPlaceholderFrag2) fragments.get(i)).fillPage();
////            }
////            if (i == frag3Pos) {
////                ((AccountPlaceholderFrag3) fragments.get(i)).fillPage();
////            }
////        }
//
//
//        AccountPlaceholderFrag1 frag1 = (AccountPlaceholderFrag1)getItem(frag1Pos);
//        frag1.fillPage();
//        notifyDataSetChanged();
//        AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2)getItem(frag2Pos);
//        frag2.fillPage();
//        notifyDataSetChanged();
//        AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3)getItem(frag3Pos);
//        frag3.fillPage();
//        notifyDataSetChanged();
//
//    }

//    public Account collectChgs() {
//        boolean areChgs;
//
//        if (myDataObject.getFrag1() == null) {
//            Log.d(TAG, "collectChgs: frag1 is null");
//        } else {
//            Log.d(TAG, "collectChgs: ready to ask of collect " + myDataObject.getAccount());
//            areChgs = myDataObject.getFrag1().collectChgs(myDataObject.getAccount());
//            Log.d(TAG, "collectChgs: areChgs " + areChgs);
//            Log.d(TAG, "collectChgs: after asking of collect " + myDataObject.getAccount());
//        }
//        if (myDataObject.getFrag2() == null) {
//            Log.d(TAG, "collectChgs: frag2 is null");
//        } else {
//            areChgs = myDataObject.getFrag2().collectChgs(myDataObject.getAccount());
//            Log.d(TAG, "collectChgs: areChgs " + areChgs);
//        }
//        if (myDataObject.getFrag3() == null) {
//            Log.d(TAG, "collectChgs: frag3 is null");
//        } else {
//            areChgs = myDataObject.getFrag3().collectChgs(myDataObject.getAccount());
//            Log.d(TAG, "collectChgs: areChgs " + areChgs);
//        }
//
//        return myDataObject.getAccount();
//    }

//    @Override
//    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        Fragment fragment = (Fragment) object;
//        if (fragment != mCurrentFragment) {
//            mCurrentFragment = ((Fragment) object);
//        }
//        super.setPrimaryItem(container, position, object);
//    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        mFragments.remove(position);
//    }

//    public boolean getItemUpdates(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
////            AccountPlaceholderFragment frag = listFrags.get(position);
////            Log.d(TAG, "getItemUpdates: " + frag.getSection());
////            return frag.verifyAcctPages(frag.getSection());
//
//        return false;
//    }


//        public void removeFrag(int position) {
//            AccountPlaceholderFragment frag = listFrags.get(position);
//            getSupportFragmentManager().beginTransaction().remove(frag).commit();
//        }

//    public int getCountBuilt() {
//        return fragments.size();
//    }
//
//    @Override
//    public int getCount() {
//        // Show 3 total pages.
//        return NUM_ITEMS;
//    }

    //        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Page 1 of 3";
//                case 1:
//                    return "Page 2 of 3";
//                case 2:
//                    return "Page 3 of 3";
//            }
//            return null;
//        }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
//        if (position <= getCount()) {
//            FragmentManager manager = ((Fragment) object).getFragmentManager();
//            FragmentTransaction trans = manager.beginTransaction();
//            trans.remove((Fragment) object);
//            trans.commit();
//        }
    }

//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
//
//    public Fragment getCurrentFragment() {
//        return mCurrentFragment;
//    }

//    public int getFrag1RowId() {
//        return frag1RowId;
//    }
//
//    public void setFrag1RowId(int frag1RowId) {
//        this.frag1RowId = frag1RowId;
//    }
//
//    public int getFrag2RowId() {
//        return frag2RowId;
//    }
//
//    public void setFrag2RowId(int frag2RowId) {
//        this.frag2RowId = frag2RowId;
//    }
//
//    public int getFrag3RowId() {
//        return frag3RowId;
//    }
//
//    public void setFrag3RowId(int frag3RowId) {
//        this.frag3RowId = frag3RowId;
//    }


//    private List<Fragment> getFragments() {
//        List<Fragment> fList = new ArrayList<Fragment>();
//        fList.add(frag1.newInstance(0));
//        fList.add(frag2.newInstance(1));
//        fList.add(frag3.newInstance(2));
//        return fList;
//    }


//    public void setMyDataObject(MyDataObject myDataObject) {
//        this.myDataObject = myDataObject;
//    }


//    public void setFrag1(AccountPlaceholderFrag1 frag1) {
//        this.frag1 = frag1;
//    }
//
//    public void setFrag2(AccountPlaceholderFrag2 frag2) {
//        this.frag2 = frag2;
//    }
//
//    public void setFrag3(AccountPlaceholderFrag3 frag3) {
//        this.frag3 = frag3;
//    }


    public void clearAll() //You can clear any specified page if you want...
    {

        Log.d(TAG, "clearAll: size " + fragments.size());
        if (fragments.size() == 0) {
            return;
        }

        for (int i = 0; i < fragments.size(); i++) {
            Log.d(TAG, "clearAll: fragItem " + fragments.get(i).getClass().getName());
            if (!fragments.get(i).getClass().getName().equals("com.kinsey.passwords.provider.RetainedFragment")) {
                mFragmentManager.beginTransaction().remove(fragments.get(i)).commit();
            }
        }
        fragments.clear();
        fragments = new ArrayList<Fragment>();
//        notifyDataSetChanged();
    }

    public void addList(List<Fragment> fragments) //Add some new fragment...
    {
//        listFrag=new ListFragment();
//        fragments.add(list);
        this.fragments = fragments;
    }
}
