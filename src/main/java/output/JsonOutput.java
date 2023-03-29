package output;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import metric.execution.MetricExecutor;
public class JsonOutput {

    public void writeResults(final String projectName, final MetricExecutor metricExecutor) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Files.copy(Path.of("src\\main\\resources\\result.json"), Path.of("src\\main\\resources\\result_backup.json"), REPLACE_EXISTING);

        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
        };
        final File resultsFile = new File("src\\main\\resources\\result4.json");

        HashMap<String, Object> resultMap;

        try {
            resultMap = objectMapper.readValue(resultsFile, typeRef);
        } catch ( final IOException e ) {
            resultMap = new HashMap<>();
        }

        resultMap.put(projectName, metricExecutor);
        objectMapper.writeValue(resultsFile, resultMap);
    }
}
