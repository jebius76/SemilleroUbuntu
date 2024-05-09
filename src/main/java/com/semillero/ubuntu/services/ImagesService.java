package com.semillero.ubuntu.services;

import com.semillero.ubuntu.entities.ImagesEntity;
import com.semillero.ubuntu.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImagesService {
    @Autowired
    ImagesRepository cloudinaryImagesRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<ImagesEntity> list (){
        return cloudinaryImagesRepository.findByOrderById();
    }

    public void save(ImagesEntity cloudinaryImagesEntity){
        cloudinaryImagesRepository.save(cloudinaryImagesEntity);
    }
    public void delete(int id){
        cloudinaryImagesRepository.deleteById(id);
    }

    public Optional<ImagesEntity> getOne(int id){
        return cloudinaryImagesRepository.findById(id);
    }
    public boolean exists(int id){
        return cloudinaryImagesRepository.existsById(id);
    }
    public void saveAll(List<ImagesEntity> imagesEntities) {
        for (ImagesEntity imagesEntity : imagesEntities) {
            save(imagesEntity);
        }
    }

}
