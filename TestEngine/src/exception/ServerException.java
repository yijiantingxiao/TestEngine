package exception;

@SuppressWarnings("serial")
public class ServerException extends RuntimeException {
	
	public ServerException(String message){
		super(message);
	}
	
}
