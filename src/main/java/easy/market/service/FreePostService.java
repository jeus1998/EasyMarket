package easy.market.service;

import easy.market.entity.Post;
import easy.market.entity.PostImage;
import easy.market.repository.post.FreePostImageRepository;
import easy.market.repository.post.FreePostRepository;
import easy.market.request.freepost.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static easy.market.entity.Post.*;
import static easy.market.entity.PostImage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreePostService {

    private static final String IMAGE_STORAGE_PATH = "images/freepost/";

    private final FreePostRepository freePostRepository;
    private final FreePostImageRepository freePostImageRepository;

    @Transactional
    public Long addPost(PostRequest postRequest) {
        String title = postRequest.getTitle();
        String content = postRequest.getContent();
        List<PostImage> postImageList = postRequest.getImages().stream()
                .map(id -> {
                    String url = saveImage(id.getFileName(), id.getBase64Data());
                    return createPostImage(id.getFileName(), url);
                }).collect(Collectors.toList());

        Post newPost = createPost(postRequest.getTitle(),
                postRequest.getContent(), postImageList);

        Post savePost = freePostRepository.save(newPost);
        return savePost.getId();
    }

    private String saveImage(String fileName, String base64Data) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            String extension = getFileExtension(fileName);
            String saveFileName = UUID.randomUUID() + "." + extension;
            Path imagePath = Paths.get(IMAGE_STORAGE_PATH + saveFileName);

            if (!Files.exists(imagePath.getParent())) {
                Files.createDirectories(imagePath.getParent());
            }

            Files.write(imagePath, imageBytes);
            return imagePath.toString();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        return fileName.substring(extensionIndex);
    }
}
