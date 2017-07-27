package wj.offer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端
 */
public class NIOClientSocket {

	public static void main(String[] args) throws IOException {
		// 1.创建SocketChannel
		SocketChannel socketChannel = SocketChannel.open();
		// 2.连接服务器
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8989));

		// 写数据
		String msg = "我是客户端~";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(msg.getBytes());
		buffer.flip();
		socketChannel.write(buffer);
		socketChannel.shutdownOutput();

		// 读数据
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		while (true) {
			buffer.clear();
			len = socketChannel.read(buffer);
			if (len == -1)
				break;
			buffer.flip();
			while (buffer.hasRemaining()) {
				bos.write(buffer.get());
			}
		}

		System.out.println("客户端收到:" + new String(bos.toByteArray()));

		socketChannel.close();

	}
}
