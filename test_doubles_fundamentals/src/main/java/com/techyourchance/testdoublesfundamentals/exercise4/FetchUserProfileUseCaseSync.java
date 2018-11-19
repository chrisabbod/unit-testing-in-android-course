package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync mUserProfileHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync,
                                       UsersCache usersCache) {
        mUserProfileHttpEndpointSync = userProfileHttpEndpointSync;
        mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        EndpointResult endpointResult;
        try {
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId);
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(
                    new User(userId, endpointResult.getFullName(), endpointResult.getImageUrl()));
            return UseCaseResult.SUCCESS;
        }else{
            if(endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR){
                return UseCaseResult.FAILURE;
            }else if(endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR){
                return UseCaseResult.FAILURE;
            }else if(endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR){
                return UseCaseResult.FAILURE;
            }
        }
        return UseCaseResult.SUCCESS;
    }

    private boolean isSuccessfulEndpointResult(EndpointResult endpointResult) {
        return endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}
