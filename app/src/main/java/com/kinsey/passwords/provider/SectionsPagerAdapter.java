package com.kinsey.passwords.provider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.kinsey.passwords.AccountPlaceholderFrag1;
import com.kinsey.passwords.AccountPlaceholderFrag2;
import com.kinsey.passwords.AccountPlaceholderFrag3;
import com.kinsey.passwords.items.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvonne on 4/27/2017.
 */
/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";
    private static final boolean DEBUG = false;

    static final int NUM_ITEMS = 3;
    int fragListPos = -1;
    int frag1Pos = 0;
    int frag2Pos = 1;
    int frag3Pos = 2;
    private Account account = new Account();
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
//    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private Fragment mCurrentFragment = null;
    private List<Fragment> mFragments;
//    private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
//    private AccountPlaceholderFrag1 frag1 = new AccountPlaceholderFrag1();
//    private AccountPlaceholderFrag2 frag2 = new AccountPlaceholderFrag2();
//    private AccountPlaceholderFrag3 frag3 = new AccountPlaceholderFrag3();
    private AccountPlaceholderFrag1 frag1;
    private AccountPlaceholderFrag2 frag2;
    private AccountPlaceholderFrag3 frag3;

//        final Context mContext;
//        final WeakReference<Fragment>[] m_fragments;
//        private SparseArray<WeakReference<Fragment>> mFragments = new SparseArray<>();
//        List<AccountPlaceholderFragment> listFrags = new ArrayList<AccountPlaceholderFragment>();

    public SectionsPagerAdapter(FragmentManager fragmentManager, Account account) {
//                                List<Fragment> fragments) {
        super(fragmentManager);
        this.mFragmentManager = fragmentManager;
//        mFragments = fragments;
        this.account = account;

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
        return 3;
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

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            AccountPlaceholderFragment frag = AccountPlaceholderFragment.newInstance(position + 1);
//            listFrags.add(frag);
//            Log.d(TAG, "getItemUpdates: " + frag.getSection());
//            return frag;
//            return AccountPlaceholderFragment.newInstance(position + 1);

//        Fragment frag = null;

        Log.d(TAG, "getItem: pos " + position);
        switch (position) {
            case 0:
                frag1 = AccountPlaceholderFrag1.newInstance(position);
                frag1.setAccount(account);
//                frag1 = AccountPlaceholderFrag1.newInstance(position, account.getId());
//                frag1 = new AccountPlaceholderFrag1();
//                Bundle arguments = new Bundle();
//                arguments.putInt(AccountPlaceholderFrag1.ARG_SECTION_NUMBER, 0);

                return frag1;
//                mFragments.add(frag1);
//                Log.d(TAG, "getItem: frag frag1 ");
//                frag1 = (AccountPlaceholderFrag1)frag;
//                frag = fragments.get("0");
//                break;
            case 1:
                frag2 = AccountPlaceholderFrag2.newInstance(position);
                return frag2;
//                frag2 = (AccountPlaceholderFrag2)frag;
//                frag = fragments.get("1");
//                break;
            case 2:
                frag3 = AccountPlaceholderFrag3.newInstance(position);
                return frag3;
//                frag3 = (AccountPlaceholderFrag3)frag;
//                frag = fragments.get("2");
//                break;
        }

//        if (frag != null) {
//            fragments.put(position, frag);
//        }
//
//        return frag;

        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        if (frag1 != null) {
            frag1.fillPage(this.account);
        }
//        AccountPlaceholderFrag2 frag2 = (AccountPlaceholderFrag2) fragments.get(1);
        if (frag2 != null) {
            frag2.fillPage(this.account);
        }
//        AccountPlaceholderFrag3 frag3 = (AccountPlaceholderFrag3) fragments.get(2);
        if (frag3 != null) {
            frag3.fillPage(this.account);
        }
        super.notifyDataSetChanged();
    }

    public void refreshPage(int position) {
        switch (position) {
            case 0:
                frag1.fillPage(account);
                break;
            case 1:
                frag2.fillPage(account);
                break;
            case 2:
                frag3.fillPage(account);
                break;
        }
    }

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

    public boolean validateFrags(int position) {
        switch (position) {
            case 0:
                frag1.validatePage1();
                break;
            case 1:
                frag2.validatePage2();
                break;
            case 2:
                frag3.validatePage3();
                break;
        }
        return true;
    }

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
//        frag1.fillPage(this.account);
//        frag2.fillPage(this.account);
//        frag3.fillPage(this.account);
//    }

    public void collectChgs() {
        boolean areChgs;
        if (frag1 == null) {
            Log.d(TAG, "collectChgs: frag1 is null");
        } else {
            areChgs = frag1.collectChgs(this.account);
            Log.d(TAG, "collectChgs: areChgs " + areChgs);
        }
        if (frag2 == null) {
            Log.d(TAG, "collectChgs: frag2 is null");
        } else {
            areChgs = frag2.collectChgs(this.account);
            Log.d(TAG, "collectChgs: areChgs " + areChgs);
        }
        if (frag3 == null) {
            Log.d(TAG, "collectChgs: frag3 is null");
        } else {
            areChgs = frag3.collectChgs(this.account);
            Log.d(TAG, "collectChgs: areChgs " + areChgs);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentFragment) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        mFragments.remove(position);
//    }

    public boolean getItemUpdates(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            AccountPlaceholderFragment frag = listFrags.get(position);
//            Log.d(TAG, "getItemUpdates: " + frag.getSection());
//            return frag.verifyAcctPages(frag.getSection());

        return false;
    }


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


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(frag1.newInstance(0));
        fList.add(frag2.newInstance(1));
        fList.add(frag3.newInstance(2));
        return fList;
    }
}
