package com.shakalinux.biblia.service;

import com.shakalinux.biblia.model.Post;
import com.shakalinux.biblia.model.Profile;
import com.shakalinux.biblia.model.User;
import com.shakalinux.biblia.model.VersiculoSalvo;
import com.shakalinux.biblia.repository.EstudoRepository;
import com.shakalinux.biblia.repository.PostRepository;
import com.shakalinux.biblia.repository.ProfileRepository;
import com.shakalinux.biblia.repository.VersiculoSalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private VersiculoSalvoRepository versiculoSalvoRepository;

    @Autowired
    private EstudoRepository estudoRepository;



    public List<Profile> listAllProfile() {
        return profileRepository.findAll();
    }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public void deletarProfile(Long id) {
        profileRepository.deleteById(id);
    }

    public Optional<Profile> findProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile findByUser(User user) {
        return profileRepository.findByUser(user);
    }

    public void updateProfileImages(Profile profile, byte[] avatar, byte[] imagePrincipal) {
        if (avatar != null && avatar.length > 0) profile.setAvatar(avatar);
        if (imagePrincipal != null && imagePrincipal.length > 0) profile.setImagePrincipal(imagePrincipal);
    }


    public void encodeImages(Profile profile) {
        if (profile.getAvatar() != null && profile.getAvatar().length > 0) {
            profile.setAvatar64(Base64.getEncoder().encodeToString(profile.getAvatar()));
        } else {
            profile.setAvatar64("");
        }

        if (profile.getImagePrincipal() != null && profile.getImagePrincipal().length > 0) {
            profile.setImagePrincipal64(Base64.getEncoder().encodeToString(profile.getImagePrincipal()));
        } else {
            profile.setImagePrincipal64("");
        }
    }

    public List<VersiculoSalvo> getVersiculosUser(User user) {
        List<VersiculoSalvo> versiculos = new ArrayList<>();

        versiculos = versiculoSalvoRepository.findByUser(user);

        return versiculos;
    }







}
