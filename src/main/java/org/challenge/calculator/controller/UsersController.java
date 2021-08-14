package org.challenge.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.model.AppUser;
import org.challenge.calculator.model.AppUserFactory;
import org.challenge.calculator.services.UserService;
import org.challenge.calculator.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller in charge of CRUD operations for User objects
 */
@SecurityRequirement(name = "calculatorapi")
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Gets the list of all users registered in the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the users found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "If the pagination parameters are wrong",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping
    public Page<AppUser> listUsers(@Parameter(description = "Zero based index that indicates the page we want to retrieve")
                                   @RequestParam(defaultValue = "0") Integer pageIndex,
                                   @Parameter(description = "Number of records per page. Default is 20.")
                                   @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(description = "Field name used to sort the returned elements. Default is username")
                                   @RequestParam(defaultValue = "username") String sortBy) {
        return AppUserFactory.buildFromPageUser(userService.listUsers(pageIndex, pageSize, sortBy));
    }

    @Operation(summary = "Retrieves the user information that corresponds to the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the found user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "404", description = "If the user wasn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping(value = "/{username}")
    public ResponseEntity<AppUser> searchUser(@PathVariable String username) {
        Optional<User> userOptional = userService.searchUser(username);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(AppUserFactory.buildFromUser(userOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity(JsonUtil.buildJsonSimpleResponse("The user couldn't be found."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Updates a user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the user with the new data.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "404", description = "If the user we want to update doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @PutMapping
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser appUser) {
        User updatedUser = userService.updateUser(AppUserFactory.buildFromAppUser(appUser));
        return new  ResponseEntity<>(AppUserFactory.buildFromUser(updatedUser), HttpStatus.OK);
    }


    @Operation(summary = "Deletes an specific user identified by it's username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returs an ACK message for the deletion",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "If the user we want to delete doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "If the username is not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username) {
        ResponseEntity<String> response;
        if (StringUtils.isNotBlank(username)) {
            userService.deleteUser(username);
            response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("User deleted successfully"), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("The received information is wrong. Please verify"), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Gets the list of all the different user roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of roles",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})})
    @GetMapping("/roles")
    public ResponseEntity<List<String>> userRoles() {
        return new ResponseEntity<>(userService.getUserRoles(), HttpStatus.OK);
    }

    /**
     * Get the list/catalog of user statuses
     */
    @Operation(summary = "Gets the list of all the different status a user could have")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of statuses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})})
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> userStatuses() {
        return new ResponseEntity<>(userService.getUserStatuses(), HttpStatus.OK);
    }

}
