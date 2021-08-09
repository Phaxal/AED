import java.util.Iterator;
import java.util.Scanner;
import java.io.IOException;

/*FILE*/
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*Folders*/
import HomeAway.*;

/**
 * @author Luis Rosa - 43612 - lm.rosa@campus.fct.unl.pt
 * @author Diogo Pereira - 44640 - dal.pereira@campus.fct.unl.pt
 */

public class Main {
	
	/* Commands */
	private static final String INSERT_USER = "IU";
	private static final String UPDATE_USER = "UU";
	private static final String REMOVE_USER = "RU";
	private static final String GET_USER = "GU";
	private static final String ADD_HOME = "AH";
	private static final String REMOVE_HOME = "RH";
	private static final String GET_HOME = "GH";
	private static final String ADD_TRAVEL = "AT";
	private static final String LIST_HOME = "LH";
	private static final String LIST_TRAVELLER = "LT";
	private static final String POLL_HOME = "PH";
	private static final String EXIT = "XS";
	private static final String LIST_BEST = "LB";
	
	/* File */
	private static final String FILENAME = "database.dat";
	
	/* Outputs */
	private static final String INSERT_SUCCESS = "Insercao de utilizador com sucesso.";
	private static final String USER_EXIST = "Utilizador existente.";
	private static final String INVALID_USER = "Utilizador inexistente.";
	private static final String UPDATE_USER_SUCCESS = "Utilizador atualizado com sucesso.";
	private static final String REMOVE_USER_SUCCESS = "Utilizador removido com sucesso.";
	private static final String REMOVE_FAIL_OWNER = "Utilizador e proprietario.";
	private static final String HOME_SUCCESS = "Propriedade adicionada com sucesso.";
	private static final String INVALID_DATA = "Dados invalidos.";
	private static final String HOME_EXIST = "Propriedade existente.";
	private static final String REMOVE_HOME_SUCCESS = "Propriedade removida com sucesso.";
	private static final String INVALID_HOME = "Propriedade inexistente.";
	private static final String ALREADY_VISITED = "Propriedade ja foi visitada.";
	private static final String TRAVELLER_IS_OWNER = "Viajante e o proprietario.";
	private static final String TRAVEL_ADD = "Estadia adicionada com sucesso.";
	private static final String TRAVELLER_IS_NOT_OWNER = "Viajante nao e o proprietario.";
	private static final String FAIL_OWNER = "Utilizador nao e proprietario.";
	private static final String USER_NOT_TRAVELLED = "Utilizador nao viajou.";
	private static final String SEARCH_RESULT_FAIL = "Pesquisa nao devolveu resultados.";
	private static final String SAVE_END = "Gravando e terminando...";

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {

		HomeAway homeaway = load();

		Scanner in = new Scanner(System.in);
		String cmd = in.next().toUpperCase().trim();

		while (!cmd.equals(EXIT)) {
			switch (cmd) {
			case INSERT_USER:
				insertUser(in, homeaway);
				break;
			case UPDATE_USER:
				updateUser(in, homeaway);
				break;
			case REMOVE_USER:
				removeUser(in, homeaway);
				break;
			case GET_USER:
				getUser(in, homeaway);
				break;
			case ADD_HOME:
				addHome(in, homeaway);
				break;
			case REMOVE_HOME:
				removeHome(in, homeaway);
				break;
			case GET_HOME:
				getHome(in, homeaway);
				break;
			case ADD_TRAVEL:
				addTravel(in, homeaway);
				break;
			case LIST_HOME:
				listHome(in, homeaway);
				break;
			case LIST_TRAVELLER:
				listTraveller(in, homeaway);
				break;
			case POLL_HOME:
				pollHome(in, homeaway);
				break;
			case LIST_BEST:
				listBest(in, homeaway);
			default:
				break;
			}
			System.out.println();
			cmd = in.next().trim().toUpperCase();
		}
		store(homeaway);
		System.out.println(Main.SAVE_END);
		System.out.println();
		in.close();
	}

	private static void insertUser(Scanner in, HomeAway homeaway) {
		// Insert a User.
		try {
			String userID = in.next().trim();
			String email = in.next().trim();
			String phone = in.next().trim();
			String name = in.nextLine().trim();
			String nationality = in.nextLine().trim();
			String address = in.nextLine().trim();
			in.nextLine();

			homeaway.insertUser(userID, email, phone,
					name, nationality, address);
			System.out.println(INSERT_SUCCESS);

		} catch (DuplicateException e) {
			System.out.println(USER_EXIST);
		}
	}

	private static void updateUser(Scanner in, HomeAway homeaway) {
		// Update a User's information.
		try {
			String userID = in.next().trim().toLowerCase();
			String email = in.next().trim();
			String phone = in.nextLine().trim();
			String address = in.nextLine().trim();
			in.nextLine();

			homeaway.updateUser(userID, email, phone, address);
			System.out.println(Main.UPDATE_USER_SUCCESS);
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		}
	}

	private static void removeUser(Scanner in, HomeAway homeaway) {
		// Remove a User. Must not own a Home.
		try {
			String userID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			homeaway.removeUser(userID);
			System.out.println(Main.REMOVE_USER_SUCCESS);

		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		} catch (UserIsOwnerException e) {
			System.out.println(Main.REMOVE_FAIL_OWNER);
		}
	}

	private static void getUser(Scanner in, HomeAway homeaway) {
		// Returns the given User's information.
		try {
			String userID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			SafeUser user = homeaway.getUser(userID);
			System.out.printf("%s: %s, %s, %s, %s\n", user.getName(),
					user.getAddress(), user.getNationality(), user.getEmail(),
					user.getPhone());
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		}
	}

	private static void addHome(Scanner in, HomeAway homeaway) {
		// Adds a Home to the given User.
		try {
			String homeID = in.next().trim();
			String userID = in.next().trim();
			int price = in.nextInt();
			int capacity = in.nextInt();
			String region = in.nextLine().trim();
			String description = in.nextLine().trim();
			String address = in.nextLine().trim();
			in.nextLine();

			if (price <= 0 || capacity <= 0 || capacity > HomeAway.MAX_CAPACITY) {
				System.out.println(INVALID_DATA);
			} else {
				homeaway.addHome(homeID, userID, price, capacity, region,
						description, address);
				System.out.println(HOME_SUCCESS);
			}
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		} catch (DuplicateException e) {
			System.out.println(Main.HOME_EXIST);
		}
	}

	private static void removeHome(Scanner in, HomeAway homeaway) {
		// Removes the given Home from the system. Home must not have Travels.
		try {
			String homeID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			homeaway.removeHome(homeID);
			System.out.println(Main.REMOVE_HOME_SUCCESS);

		} catch (HomeNotFoundException e) {
			System.out.println(Main.INVALID_HOME);
		} catch (HomeHasTravelException e) {
			System.out.println(Main.ALREADY_VISITED);
		}
	}

	private static void getHome(Scanner in, HomeAway homeaway) {
		// Returns the given Home's information.
		try {
			String homeID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			SafeHome home = homeaway.getHome(homeID);
			System.out.printf("%s: %s, %s, %d, %d, %d, %s\n", home.getDescription(), home.getAddress(), 
					home.getRegion(), home.getPrice(), home.getCapacity(), 
					home.getTotalScore(), home.getUser().getName());
		} catch (HomeNotFoundException e) {
			System.out.println(Main.INVALID_HOME);
		}
	}

	private static void addTravel(Scanner in, HomeAway homeaway) {
		// Adds a Travel to the given Home.
		try {
			String line = in.nextLine().trim();
			in.nextLine();

			String[] input = line.split(" ");
			String userID = input[0].toLowerCase();
			String homeID = input[1].toLowerCase();

			if (input.length == 3 && (Integer.parseInt(input[2]) <= 0 || Integer.parseInt(input[2]) > HomeAway.MAX_TRAVEL_SCORE))
				System.out.println(Main.INVALID_DATA);
			else if (!homeaway.hasUser(userID))
				System.out.println(Main.INVALID_USER);
			else if (!homeaway.hasHome(homeID))
				System.out.println(Main.INVALID_HOME);
			else if (input.length == 3 && homeaway.userOwnsHome(userID, homeID))
				System.out.println(Main.TRAVELLER_IS_OWNER);
			else if (input.length == 2 && !homeaway.userOwnsHome(userID, homeID))
				System.out.println(Main.TRAVELLER_IS_NOT_OWNER);
			else {
				System.out.println(Main.TRAVEL_ADD);
				if (input.length == 3)
					homeaway.addTravel(userID, homeID, Integer.parseInt(input[2]));
				else
					homeaway.addTravel(userID, homeID, 0);
			}
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		} catch (HomeNotFoundException e) {
			System.out.println(Main.INVALID_HOME);
		}

	}

	private static void listHome(Scanner in, HomeAway homeaway) {
		// Prints all the Homes the given User has.
		try {
			String userID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			Iterator<SafeHome> homes = homeaway.listHome(userID);
			while (homes.hasNext()){
				SafeHome home = homes.next();
				printHomeStatus(home);
			}
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		} catch (HomeNotFoundException e) {
			System.out.println(Main.FAIL_OWNER);
		}
	}

	private static void listTraveller(Scanner in, HomeAway homeaway) {
		// Prints all the Homes visited by the given traveller.
		try {
			String userID = in.nextLine().trim().toLowerCase();
			in.nextLine();

			Iterator<Travel> it = homeaway.listTravels(userID);
			while (it.hasNext()) {
				SafeHome home = it.next().getHome();
				printHomeStatus(home);
			}
		} catch (UserNotFoundException e) {
			System.out.println(Main.INVALID_USER);
		} catch (UserNotTravellerException e) {
			System.out.println(Main.USER_NOT_TRAVELLED);
		}
	}

	private static void pollHome(Scanner in, HomeAway homeaway) {
		// Search the Homes on a certain region with the given capacity
		try {
			int capacity = in.nextInt();
			String region = in.nextLine().trim();
			in.nextLine();

			if (capacity <= 0 || capacity > HomeAway.MAX_CAPACITY)
				System.out.println(Main.INVALID_DATA);
			else {

				Iterator<SafeHome> homes = homeaway.pollHome(capacity, region);
				while(homes.hasNext()){
					SafeHome home = homes.next();
					printHomeStatus(home);
				}
			}
		} catch (RegionNotFoundException | NoCapacityException e) {
			System.out.println(Main.SEARCH_RESULT_FAIL);
		}
	}

	private static void listBest(Scanner in, HomeAway homeaway) {
		// Prints all Homes to rent in a certain local ordered by score and ID
		try {
			String region = in.nextLine().trim();
			in.nextLine();

			Iterator<SafeHome> homes = homeaway.listBest(region);
			
			while(homes.hasNext()){
				SafeHome home = homes.next();
				printHomeStatus(home);
			}
	
		} catch (RegionNotFoundException | HomeNotFoundException e) {
			System.out.println(Main.SEARCH_RESULT_FAIL);
		}
	}

	private static void store(HomeAway homeaway) throws IOException {
		//Stores this HomeAway object in a file.
		try {
			ObjectOutputStream file = new ObjectOutputStream(
					new FileOutputStream(FILENAME));
			file.writeObject(homeaway);
			file.flush();
			file.close();
		} catch (IOException e) {
			throw new IOException();
		}
	}

	private static HomeAway load() {
		//Loads a HomeAway object from a file or creates a new one if no file is found.
		try {
			HomeAway homeaway;
			ObjectInputStream file = new ObjectInputStream(new FileInputStream(
					FILENAME));
			homeaway = (HomeAway) file.readObject();
			file.close();
			return homeaway;
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return new HomeAwayClass();
	}

	private static void printHomeStatus(SafeHome home){
		//Auxiliary method to print the status of a SafeHome.
		System.out.printf("%s %s %s %s %d %d %d\n", home.getID(), home.getDescription(), home.getAddress(), 
				home.getRegion(), home.getPrice(), home.getCapacity(), home.getTotalScore());
	}
}