package blockchain;

import java.util.Random;

class MessageTextGenerator {

    private static String[] messages = {
            "The fox in the top hat whispered into the ear of the rabbit",
            "The knives were out and she was sharpening hers",
            "The crowd yells and screams for more memes",
            "I purchased a baby clown from the Russian terrorist black market",
            "I hope that, when I've built up my savings, I'll be able to travel to Mexico",
            "Potato wedges probably are not best for relationships",
            "There is a fly in the car with us",
            "He was 100% into fasting with her until he understood that meant he couldn't eat",
            "If the Easter Bunny and the Tooth Fairy had babies would they take your teeth and " +
                    "leave chocolate for you?",
            "If I don’t like something, I’ll stay away from it.",
            "Love is not like pizza",
            "Your girlfriend bought your favorite cookie crisp cereal but forgot to get milk",
            "Please wait outside of the house",
            "I hear that Nancy is very pretty",
            "She saw no irony asking me to change but wanting me to accept her for who she is",
            "If you like tuna and tomato sauce- try combining the two. It’s really not as bad as it sounds",
            "We need to rent a room for our party",
            "Would you like to travel with me?",
            "There are few things better in life than a slice of pie",
    };

    static String getRandomMessageText() {
        Random random = new Random();
        return messages[random.nextInt(messages.length)];
    }
}
