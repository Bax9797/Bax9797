package pl.kurs.java.test.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final Configuration configuration;


    public String fillTemplate(String templateName, Object map) throws IOException, TemplateException {
        URL resource = getClass().getClassLoader().getResource("templates/" + templateName + ".ftl");
        Reader reader = new InputStreamReader(resource.openStream());
        return renderTemplate(templateName, map, reader);
    }


    public String renderTemplate(String templateName, Object map, Reader reader) throws IOException, TemplateException {
        Template template = new Template(templateName, reader, configuration);
        return processTemplate(template, map);
    }


    private String processTemplate(Template template, Object map) throws TemplateException, IOException {
        StringWriter writer = new StringWriter();
        template.process(map, writer);
        return writer.toString();
    }
}
