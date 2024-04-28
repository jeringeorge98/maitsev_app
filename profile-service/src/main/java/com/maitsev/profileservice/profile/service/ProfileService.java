package com.maitsev.profileservice.profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.maitsev.postservice.post.dto.PostDto;
import com.maitsev.postservice.post.model.Post;
import com.maitsev.profileservice.profile.dto.ProfileDto;
import com.maitsev.profileservice.profile.model.Profile;
import com.maitsev.profileservice.profile.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profileRepository.findAll().forEach(profiles::add);
        return profiles.stream().map(this::mapToProfileDto).toList();
    }

    private ProfileDto mapToProfileDto(Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .password(profile.getPassword())
                .build();
    }

    public void addProfile(ProfileDto profileDto) {
        Profile profile = Profile.builder()
                .id(profileDto.getId())
                .username(profileDto.getUsername())
                .password(profileDto.getPassword())
                .build();
        profileRepository.save(profile);
        log.info("Profile {} is added to the Database", profile.getId());
    }

    public Optional<ProfileDto> getProfile(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.map(this::mapToProfileDto);
    }

    public void deleteProfile(String id) {
        profileRepository.deleteById(id);
        log.info("A Profile has been deleted");
    }

    public void updateProfile(String id, ProfileDto profileDto) {
        Profile profile = Profile.builder()
                .id(profileDto.getId())
                .username(profileDto.getUsername())
                .password(profileDto.getPassword())
                .build();
        profileRepository.save(profile);
        log.info("Profile {} is updated", profile.getId());
    }

    public List<PostDto> getProfileAllPosts(String id) {
        List<PostDto> allPosts = webClientBuilder
                .build()
                .get()
                // .uri("http://localhost:8001/api/posts")
                .uri("http://post-service/api/posts/")
                .retrieve()
                .bodyToFlux(PostDto.class)
                .collectList()
                .block();

        return allPosts.stream()
                .filter(post -> id.equals(post.getPostedById()))
                .collect(Collectors.toList());
    }

    public Optional<PostDto> getSpecificProfilePost(String id, String postId) {
        List<PostDto> allPosts = getProfileAllPosts(id);
        return allPosts.stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst();
    }

}
