package org.n3r.eql.parser;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.n3r.eql.map.EqlRun;
import org.n3r.eql.util.P;

import java.io.StringWriter;
import java.util.Map;


public class FreemarkerSql implements Sql {
    private final Template ftlTemplate;

    public FreemarkerSql(Configuration ftlConfig, Template ftlTemplate) {
        this.ftlTemplate = ftlTemplate;
    }

    @Override
    public String evalSql(EqlRun eqlRun) {
        return process(eqlRun);
    }

    public String process(EqlRun eqlRun) {
        StringWriter writer = new StringWriter();
        try {
            Map<String, Object> executionContext = eqlRun.getExecutionContext();
            Map<String, Object> rootMap = P.mergeProperties(executionContext, eqlRun.getParamBean());
            ftlTemplate.process(rootMap, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

}
