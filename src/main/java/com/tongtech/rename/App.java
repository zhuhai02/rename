package com.tongtech.rename;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        String line = "~org\\.apache\\.bookkeeper.client.api.Producer;";
//        String modifiedLine = line.replace("org\\.apache\\.pulsar", "com\\.tongtech\\.cnmq")
//                .replace("Pulsar", "Cnmq")
//                .replace("pulsar", "cnmq")
//                .replace("PULSAR", "CNMQ");
//        System.out.println(line);

        List<String> list = Arrays.asList("import com.tongtech.cnmq.client.api.Authentication;",
                "import com.tongtech.cnmq.client.api.CnmqClientException;",
                "import com.tongtech.cnmq.client.api.CnmqClientException.UnsupportedAuthenticationException;",
                "import java.util.Map;",
                "import java.util.Set;",
                "import java.util.concurrent.TimeUnit;");
        List<String> collect = list.stream().map(s -> s.replace(";", "")).collect(Collectors.toList());
        Collections.sort(collect);
        for (String s : collect) {
            System.out.println(s);
        }
    }
}
