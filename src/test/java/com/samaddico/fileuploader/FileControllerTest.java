package com.samaddico.fileuploader;

import com.samaddico.fileuploader.model.File;
import com.samaddico.fileuploader.service.FileService;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private File file;

    @Autowired
    private FileService fileService;

    @Before
    public void setUp() {
        fileService.deleteAll();//empty the table
        file = new File("image.png", "png");
        fileService.save(file);
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testFileUpload() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "data.txt", "text/plain", "DATADATADATDATADATA".getBytes());

        this.mvc
                .perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", equalTo(true)))
                .andExpect(jsonPath("result.title", equalTo(mockFile.getOriginalFilename())));
    }

    @Test
    public void testMissingFileUpload() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.multipart("/upload"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", equalTo(false)));
    }

    @Test
    public void testFind() throws Exception {
        this.mvc.perform(get(
                "/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", equalTo(1)));
    }

}
