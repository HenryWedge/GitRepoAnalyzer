package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import metric.execution.MetricExecutor;
public class JsonOutput {
    final String outFile;

    public JsonOutput(final String outFile) {
        this.outFile = outFile;
    }

    public void writeResults(final String projectName, final MetricExecutor metricExecutor) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        //Files.copy(Path.of(outFile), Path.of("src\\main\\resources\\result_backup.json"), REPLACE_EXISTING);
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
        };
        final File resultsFile = new File(outFile);

        HashMap<String, Object> resultMap;
        try {
            resultMap = objectMapper.readValue(resultsFile, typeRef);
        } catch ( final FileNotFoundException e ) {
            resultMap = new HashMap<>();
        }
        resultMap.put(projectName, metricExecutor);
        objectMapper.writeValue(resultsFile, resultMap);
    }
}
