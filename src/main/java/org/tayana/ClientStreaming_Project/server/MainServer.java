package org.tayana.ClientStreaming_Project.server;

import java.io.IOException;

import com.tayana.proto.ClientStreamingGrpc.ClientStreamingImplBase;
import com.tayana.proto.Request;
import com.tayana.proto.Response;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class MainServer 
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		Server  server=ServerBuilder
				.forPort(8082)
				.addService(new ClientStreamingImpl())
				.build();
		server.start();
		System.out.println("server started");
		server.awaitTermination();
		
	}

}

class ClientStreamingImpl extends ClientStreamingImplBase
{

	@Override
	public StreamObserver<Request> hello(StreamObserver<Response> responseObserver)
	{
		
		return new StreamObserver<Request>() {
			int sum=0;
			
			@Override
			public void onNext(Request request) 
			{
				System.out.println("message received form:"+request.getNumber());
				sum+=request.getNumber();
			}
			
			@Override
			public void onError(Throwable t)
			{
				t.printStackTrace();
			}
			
			@Override
			public void onCompleted()
			{
				
				Response response=Response.newBuilder().setResult(sum).build();
			//	System.out.println("all client requests are completed " +response);
				responseObserver.onNext(response);;
			}
		};
		
	}
	
	
}
