package com.hg.tl_hg.ctrl;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hg.tl_hg.configure.FileServerConfig;

import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import cn.lfy.common.util.UUIDUtil;

@Controller
public class FileServerCtrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileServerCtrl.class);
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
    @RequestMapping("fileUpload")
    @ResponseBody 
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file, String type, String subType) {
    	Result<String> result = Result.success();
    	LOGGER.info("File Upload type:{} subType:{}", type, subType);
        if(file.isEmpty()) {
            throw GlobalException.newGlobalException(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUIDUtil.uuid() + suffix;
        String fileServerPath = fileServerConfig.getPath();
        String filePath =  "/" + type + "/" + subType + "/" + fileName;
        File dest = new File(fileServerPath + filePath);
        if(!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            result.setData(filePath);
        } catch (Throwable e) {
           throw GlobalException.newGlobalException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        return result;
    }
    
    @RequestMapping("/fileserverLocal/{type}/{subType}/{filename}")
    public ResponseEntity<byte[]> download(HttpServletResponse response,
    		@PathVariable(name = "type") String type, 
    		@PathVariable(name = "subType") String subType, 
    		@PathVariable(name = "filename") String filename) throws IOException {
    	LOGGER.info("File Download {}/{}/{}", type, subType, filename);
    	String fileServerPath = fileServerConfig.getPath();
    	String filePath =  "/" + type + "/" + subType + "/" + filename;
    	HttpHeaders header = new HttpHeaders();
    	String fileName = new String(filename);
    	header.setContentDispositionFormData("attachment", fileName);
    	header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	File dest = new File(fileServerPath + filePath);
    	return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(dest), header, HttpStatus.OK);
    }
}
