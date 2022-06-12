package com.file.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.payloads.FileResponse;
import com.file.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PutMapping("/upload")
	public ResponseEntity<FileResponse> fileUpload(@RequestParam("image") MultipartFile image) {
		String uploadImage;
		try {
			uploadImage = this.fileService.uploadImage(path, image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<FileResponse>(
					new FileResponse(null, "Image is not uploaded due to some server error !!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<FileResponse>(new FileResponse(uploadImage, "Image is successfully uploaded !!"),
				HttpStatus.OK);
	}

	// method to serve file
	@GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE )
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
		
		InputStream resource =  this.fileService.getImage(path, imageName);
// InputStream resource =  this.fileService.getResource(path, imageName); // we write this one
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

	// localhost:8080/images/abc.png
}
