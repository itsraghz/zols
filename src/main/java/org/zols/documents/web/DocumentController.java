package org.zols.documents.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zols.documents.domain.Document;
import org.zols.documents.domain.Upload;
import org.zols.documents.service.DocumentService;

@RestController
@RequestMapping(value = "/api/documents")
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DocumentController.class);

    @Autowired
    DocumentService documentService;

    @RequestMapping(value = "/{documentRepositoryName}/**", method = RequestMethod.GET)
    public List<Document> list(@PathVariable(value = "documentRepositoryName") String documentRepositoryName, HttpServletRequest request) {
        return documentService.list(documentRepositoryName,getFolderPath(documentRepositoryName, request));
    }

    @RequestMapping(value = "/{documentRepositoryName}/**", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void upload(@PathVariable(value = "documentRepositoryName") String documentRepositoryName, 
            @ModelAttribute("document") Upload document) {        
        documentService.upload(documentRepositoryName, document);        
    }

    /**
     * gets Folder path from request url
     *
     * @param documentRepositoryName
     * @param request
     * @return null - if no folder path found
     */
    private String getFolderPath(String documentRepositoryName, HttpServletRequest request) {
        String folderPath = request.getRequestURL().toString();
        String textToMatch = "/api/documents/" + documentRepositoryName + "/";
        int startPointOdFolderPath = folderPath.indexOf(textToMatch);
        if (startPointOdFolderPath != -1) {
            return folderPath.substring(startPointOdFolderPath + textToMatch.length());
        } else {
            return null;
        }
    }
}
