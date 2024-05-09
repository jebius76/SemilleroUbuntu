package com.semillero.ubuntu.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class CloudinaryService {
    Cloudinary cloudinary;
    private Map<String, String> valuesMap = new HashMap<>();

    public CloudinaryService(@Value("${CLOUD_NAME}") String cloudName,
                             @Value("${API_KEY}") String apiKey,
                             @Value("${API_SECRET}") String apiSecret) {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", cloudName);
        valuesMap.put("api_key", apiKey);
        valuesMap.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(valuesMap);
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result;
    }


    public Map delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return null;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }

    public String getPublicIdFromUrl(String url) {
        try {
            // Parsea la URL para extraer el ID de la imagen
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] pathParts = path.split("/");
            // El ID de la imagen es el último segmento del path sin la extensión
            return pathParts[pathParts.length - 1].split("\\.")[0];
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
