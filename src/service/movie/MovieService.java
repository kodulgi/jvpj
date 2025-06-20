package service.movie;

public interface MovieService {
    boolean registerMovie();
    boolean exists(String title);
}
