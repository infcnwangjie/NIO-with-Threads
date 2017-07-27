package wj.offer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 读操作的工具类
 * 
 * @author MOTUI
 *
 */
public class RequestProcessor {
	// 构造线程池
	private static ExecutorService executorService = Executors
			.newFixedThreadPool(10);

	public static void ProcessorRequest(final SelectionKey key,
			final Selector selector) {
		// 获得线程并执行
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("开始读");
					SocketChannel readChannel = (SocketChannel) key.channel();
					// I/O读数据操作
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int len = 0;
					while (true) {
						buffer.clear();
						len = readChannel.read(buffer);
						if (len == -1)
							break;
						buffer.flip();
						while (buffer.hasRemaining()) {
							baos.write(buffer.get());
						}
					}
					System.out.println("服务器端接收到的数据："
							+ new String(baos.toByteArray()));

					System.out.println("开始注册------");
					// 注册写操作
					readChannel.register(selector, SelectionKey.OP_WRITE);
					System.out.println("注册完成-------");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
