package xxx.examples;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

public class SumWordLength {

	public static void main(String[] args) throws Exception {

		// Checking input parameters
		final ParameterTool params = ParameterTool.fromArgs(args);

		// set up the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// make parameters available in the web interface
		env.getConfig().setGlobalJobParameters(params);

		// read the text file from given input path
		DataStream<String> text = env.readTextFile(params.get("input"));

		DataStream<Integer> parsed = text.map(String::length).filter(value -> value > 1).countWindowAll(200).sum(0);

		parsed.writeAsText(params.get("output"), WriteMode.OVERWRITE).setParallelism(1);

		AllWindowedStream<Integer, GlobalWindow> windowed = parsed.countWindowAll(200);

		SingleOutputStreamOperator<Integer> summed = windowed.sum(0);

		// emit result
		summed.writeAsText(params.get("output"), WriteMode.OVERWRITE).setParallelism(1);

		// execute program
		env.execute("Streaming SumWorkLength");
	}

}
