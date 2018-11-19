package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_URL = "imageUrl";

    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;
    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setup(){
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    //Pass userId to fetchUserProfileSync, it checks that the id is properly cached, then returns the user profile

    @Test
    public void fetchUserProfileSync_userIdPassedToEndpoint_returnSuccess(){
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndpointSyncTd.mUserId, is(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_userIdCached_returnSuccess(){
        SUT.fetchUserProfileSync(USER_ID);
        User cachedUser = usersCacheTd.getUser(USER_ID);
        assertThat(cachedUser.getUserId(), is(USER_ID));
        assertThat(cachedUser.getFullName(), is(FULL_NAME));
        assertThat(cachedUser.getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void fetchUserProfileSync_userIdNotCached_returnAuthError(){
        userProfileHttpEndpointSyncTd.mAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_userIdNotCached_returnServerError(){
        userProfileHttpEndpointSyncTd.mServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_userIdNotCached_returnGeneralError(){
        userProfileHttpEndpointSyncTd.mGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_useCaseResultSuccess_returnSuccess(){
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchUserProfileSync_useCaseResultAuthError_returnAuthError(){
        userProfileHttpEndpointSyncTd.mAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_useCaseResultServerError_returnServerError(){
        userProfileHttpEndpointSyncTd.mServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_useCaseResultGeneralError_returnGeneralError(){
        userProfileHttpEndpointSyncTd.mGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_useCaseResultNetworkError_throwNetworkErrorException(){
        userProfileHttpEndpointSyncTd.mNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    //----------------------------------------------------------------------------------------------
    //Helper Classes

    public static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String mUserId = USER_ID;
        public boolean mAuthError;
        public boolean mServerError;
        public boolean mGeneralError;
        public boolean mNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if(mAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            }else if(mServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            }else if(mGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            }else if(mNetworkError) {
                throw new NetworkErrorException();
            }else{
                return new EndpointResult(EndpointResultStatus.SUCCESS, mUserId, FULL_NAME, IMAGE_URL);
            }
        }
    }

    public static class UsersCacheTd implements UsersCache {

        private List<User> mUsers = new ArrayList<>();

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if(existingUser != null){
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for(User user: mUsers){
                if(user.getUserId().equals(userId)){
                    return user;
                }
            }
            return null;
        }
    }
}