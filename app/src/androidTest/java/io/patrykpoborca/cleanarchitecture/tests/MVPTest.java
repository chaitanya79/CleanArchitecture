package io.patrykpoborca.cleanarchitecture.tests;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import io.patrykpoborca.cleanarchitecture.TestHelper;
import io.patrykpoborca.cleanarchitecture.mockimpl.MockLocalDataCache;
import io.patrykpoborca.cleanarchitecture.mockimpl.MockTweeterActivityPview;
import io.patrykpoborca.cleanarchitecture.mockimpl.MockOkHTTP;
import io.patrykpoborca.cleanarchitecture.mockimpl.MockRetrofit;
import io.patrykpoborca.cleanarchitecture.ui.MVP.interfaces.TweeterMVPPresenter;

@RunWith(AndroidJUnit4.class)
public class MVPTest {

    private static final String SOME_URL = "SOME_URL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_NAME = "USER_NAME";

    @Inject
    MockTweeterActivityPview pView;

    @Inject
    TweeterMVPPresenter presenter;

    @Before
    public void setUp(){
        TestHelper.getTestClassInjector()
                .inject(this);
        Assert.assertTrue(TestHelper.getBaseComponent().getLocalDataCache() instanceof MockLocalDataCache);
        Assert.assertTrue(TestHelper.getBaseComponent().getRetrofit() instanceof MockRetrofit);
        Assert.assertTrue(TestHelper.getBaseComponent().getOkHTTP() instanceof MockOkHTTP);
    }


    @Test
    public void testLogin(){
        presenter.toggleLogin(USER_NAME, USER_PASSWORD);
        TestHelper.waitFor(() -> pView.toggleLoginContainerCalled);
        Assert.assertTrue(pView.toggleLoginContainerCalled);

        pView.toggleLoginContainerCalled = false;

        presenter.toggleLogin(USER_NAME, USER_PASSWORD);
        TestHelper.waitFor(() -> pView.toggleLoginContainerCalled);
        Assert.assertTrue(pView.toggleLoginContainerCalled);
    }

    @Test
    public void testFetchTweets(){
        presenter.fetchCurrentTweet();
        TestHelper.waitFor(() -> pView.fetchedTweet != null);
        String tweet = pView.fetchedTweet;

        presenter.fetchPreviousTweets();
        TestHelper.waitFor(() -> pView.previousTweets != null);
        boolean found = false;
        for(int i=0; i < pView.previousTweets.size(); i++){
            found = tweet.contains(pView.previousTweets.get(i));
            if(found){
                break;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void testWebPage() {
        presenter.loadWebPage(SOME_URL);

        TestHelper.waitFor(() -> pView.displayWebpage != null);
        String webPage = pView.displayWebpage;
        Assert.assertTrue(webPage.contains(SOME_URL));
    }

}
