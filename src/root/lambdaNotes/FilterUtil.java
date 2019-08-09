package root.lambdaNotes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FilterUtil {
	/**
	 * Approach 1: Create Methods That Search for Members That Match One
	 * Characteristic
	 */
	public static void printPersonsOlderThan(List<Person> roster, int age) {
		for (Person p : roster)
		{
			if (p.getAge() >= age)
			{
				p.printPerson();
			}
		}
	}

	/**
	 * Approach 2: Create Methods That Search for Members That Match Specified
	 * Search Criteria
	 */
	public static void printPersons(List<Person> roster, Predicate<Person> tester) {
		for (Person p : roster)
		{
			if (tester.test(p))
			{
				p.printPerson();
			}
		}
	}

	/**
	 * Approach 3: Create Methods That Search for Members That Match Specified
	 * Search Criteria and take specified action over them
	 */
	public static void processPersons(List<Person> roster, Predicate<Person> tester, Consumer<Person> block) {
		for (Person p : roster)
		{
			if (tester.test(p))
			{
				block.accept(p);
			}
		}
	}

	public static void main(String... a) {
		List<Person> roster = Person.createRoster();

		// Approach 2#A: Specify Search Criteria Code in an Anonymous Class
		printPersons(roster, new Predicate<Person>() {
			public boolean test(Person p) {
				return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			}
		});

		// Approach 2#B: Specify Search Criteria Code with a Lambda Expression
		{
			// Syntax Approach 2#B.0: standard approach
			printPersons(roster, (Person p) ->
			{
				return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			});

			// Syntax Approach 2#B.1: you can omit the data type of the
			// parameters
			printPersons(roster, (p) ->
			{
				return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			});

			// Syntax Approach 2#B.2: omit the parentheses() as there is only
			// one parameter
			printPersons(roster, p ->
			{
				return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			});

			// Syntax Approach 2#B.3: return statement can be omitted because If
			// you specify a single expression, then the JRE evaluates the
			// expression and then returns its value
			// and for expressions braces ({}) can be omitted too.
			printPersons(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
		}

		// Approach 3#A:Use Lambda Expressions Throughout Your Application,
		// Specify Search Criteria and Action Code with a Lambda Expression
		{
			// Syntax Approach 3#A.0:
			processPersons(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25, p ->
			{
				p.printPerson();
			});

			// Syntax Approach 3#A.0: because a single method call can be
			// written as expression, parenthesis{} can be omitted.
			processPersons(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25,
					p -> p.printPerson());

		}

	}
}
