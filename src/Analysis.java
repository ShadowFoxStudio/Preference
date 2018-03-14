import java.util.ArrayList;

public class Analysis {

    //анализируем карты и возвращаем заяку на контракт
    public String getBid(ArrayList<Card> deck){
        String bid;                      //ставка
        int numberOfWinCardsInDeck = 0;  //общее кол-во выигрышных карт
        int winSuit =0;                  //индекс выигрышной масти
        int winCards = 0;                //кол-во выигрышных карт в масти
        ArrayList<Card> suit = new ArrayList<>(); //карты одной масти
        for(int i=0; i<deck.size(); i++){
            if( i+1 < deck.size() && deck.get(i).getSuit() == deck.get(i+1).getSuit())
                suit.add(deck.get(i));
            else{
                suit.add(deck.get(i));
                int numberOfWinCardsInSuit = 0;   //кол-во выигрышных карт в масти
                int numberOfLosingCards = 0;      //кол-во проигрышных карт
                boolean wasPreviousCard = true;   //бала ли предыдущая карта на ранг выше текущей
                for(int rankOfCard=7, j=0; j < suit.size(); rankOfCard--){
                    //проверяем, равен ли ранг карты в руке рангу карты от туза до 7
                    if(suit.get(j).getRank() == rankOfCard){
                        if(numberOfLosingCards >= 0){        //если нет проигранных карт
                            if(wasPreviousCard){
                                numberOfWinCardsInSuit++;    //выигрываем одну карту
                            }
                        }
                        else numberOfLosingCards++;    //отбиваем одну карту
                        j++;                           //переходим к следующей карте в масти
                        wasPreviousCard = true;
                    }
                    else {
                        numberOfLosingCards--;         //проигрываем одну карту
                        wasPreviousCard = false;
                    }
                }
                if(winCards < numberOfWinCardsInSuit){
                    winCards = numberOfWinCardsInSuit;
                    winSuit = suit.get(0).getSuit();
                }
                numberOfWinCardsInDeck += numberOfWinCardsInSuit;
                suit = new ArrayList<>();
            }
        }
        if(numberOfWinCardsInDeck >=5){   // от 5 - риск на получение нужных карт в прикупе
            if(numberOfWinCardsInDeck == 5) numberOfWinCardsInDeck +=1; //заявляем 6
            bid = String.valueOf(numberOfWinCardsInDeck);
            if(winCards > 3){
                switch (winSuit){
                    case 0:
                        bid += "\u2660";  //пики
                        break;
                    case 1:
                        bid += "\u2667";  //крести
                        break;
                    case 2:
                        bid += "\u2662";  //бубны
                        break;
                    case 3:
                        bid += "\u2661";  //червы
                        break;
                }
            }
            else bid += "Б.К.";
        }
        else bid = "";
        return bid;
    }

    //получаем окончательный контракт
    public ArrayList<String> getFinalContract(ArrayList<Card> deck, History history){
        ArrayList<String> finalContract = new ArrayList<>();
        ArrayList<Card> drop = new ArrayList<>();
        ArrayList<Card> cards = deck;
        String contract = getBid(deck);
        //если был мизер, то удаляем 2 самые большие карты (по рангу)
        if(contract.contains("Мизер")){
            for(int i=0; i<2; i++) {
                deck.remove(cards.get(i));
                drop.add(cards.get(i));
                finalContract.add("Мизер");
            }
        }
        //если была заявка без козыря, то удаляем 2 самые мелкие карты (по рангу)
        else if(contract.contains("Б.К.")){
            for(int i = cards.size()-1; i > cards.size()-3; i--) {
                deck.remove(cards.get(i));
                drop.add(cards.get(i));
                finalContract.add(contract.substring(0, contract.indexOf("Б")));
                finalContract.add("Б.К.");
            }
        }
        //если была заявка с козырем, то удаляем 2 самые мелкие карты (по рангу), которые не являются козырем
        else{
            int suit=0;
            String str ="";
            //определяем козырь
            if(contract.contains(Suits.SPADES.value)) {
                suit = 0;
                str = Suits.SPADES.value;
            }
            if(contract.contains(Suits.CLUBS.value)) {
                suit = 1;
                str = Suits.CLUBS.value;
            }
            if(contract.contains(Suits.DIAMONDS.value)) {
                suit = 2;
                str = Suits.DIAMONDS.value;
            }
            if(contract.contains(Suits.HEARTS.value)) {
                suit = 3;
                str = Suits.HEARTS.value;
            }
            for(int i = cards.size()-1 , n=0; i > 0; i--) {
                if(cards.get(i).getSuit() != suit) {
                    deck.remove(cards.get(i));
                    drop.add(cards.get(i));
                    n++;
                }
                if(n == 2) break; //когда удалили 2 карты
            }
            finalContract.add(contract.substring(0, contract.indexOf(str)));
            finalContract.add(str);

        }
        for(int i=0; i<2; i++) history.setHistoryOfDrop(drop.get(i));
        return finalContract;
    }

    enum Suits{
        SPADES("\u2660"), CLUBS("\u2667"), DIAMONDS("\u2662"), HEARTS("\u2661");

        String value;

        Suits(String value) {
            this.value = value;
        }
    }
}
