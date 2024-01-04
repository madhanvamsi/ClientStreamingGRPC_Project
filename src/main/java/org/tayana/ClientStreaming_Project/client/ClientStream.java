package org.tayana.ClientStreaming_Project.client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.tayana.proto.ClientStreamingGrpc;
import com.tayana.proto.Request;
import com.tayana.proto.Response;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ClientStream
{
	public static void main(String[] args) 
	{
		ManagedChannel channel=ManagedChannelBuilder.forAddress("localhost", 8082)
				.usePlaintext()
				.build();
		CountDownLatch latch = new CountDownLatch(1);
		ClientStreamingGrpc.ClientStreamingStub  Stub=ClientStreamingGrpc.newStub(channel);
		
		StreamObserver<Request> observer=Stub.hello(
	new StreamObserver<Response>()
		{
			
			@Override
			public void onNext(Response response)
			{
				System.out.println("result :"+response.getResult());
			}
			
			@Override
			public void onError(Throwable t)
			{
				t.printStackTrace();
				latch.countDown();
			}
			
			@Override
			public void onCompleted() 
			{
				latch.countDown();
			}
		});
		
		List<Integer> number=Arrays.asList(1,2,3,4,5);
		for(Integer num:number)
		{
			Request  request=Request.newBuilder().setNumber(num).build();
			observer.onNext(request);
		}
		observer.onCompleted();
		
		 try 
		 {
	         latch.await();
	         channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	     } 
		 catch (InterruptedException | RuntimeException e)
		 {
	         e.printStackTrace();
	     }
	}
	
}
