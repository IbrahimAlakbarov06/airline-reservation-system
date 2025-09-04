package org.airline.msbooking.client;

import org.airline.msbooking.model.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-user", path = "api/users")
public interface UserClient {

    @GetMapping("/profile")
    UserProfileResponse getCurrentUserProfile(@RequestHeader("X-User-Id") String userIdHeader);

}
