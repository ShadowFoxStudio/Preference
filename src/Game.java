import java.util.ArrayList;

public class Game {
    private int round;                                    //номер сдачи
    private int firsthand;                                //номер "первой руки"
    private Bot westBot, eastBot, southBot;               //боты
    private Gamestat gamestat;                            //статус игры
    private boolean somebodyIsPassed;                     //кто-либо спасовал
    public History history;
    private Analysis analysis;
    
    enum Gamestat{
        IS_DEAL,            //идут торги
        IS_PLAY,            //идет игра
        IS_ALL_PASS,        //если все спасовали
        IS_END              //конец игры
    }

    Game(){
        westBot = new Bot("West");
        eastBot = new Bot("East");
        southBot = new Bot("South");
        round = 1;
        firsthand = 1;
        somebodyIsPassed = false;
        history = new History();
        analysis = new Analysis();
    }

    //раздача карт
    private void distribution(){
        Croupier cr = new Croupier();
        //раздаём карты ботам
        for(int i=0; i<10; i++){
            westBot.addCard(cr.getCardFromDeck());
            eastBot.addCard(cr.getCardFromDeck());
            southBot.addCard(cr.getCardFromDeck());
        }
        //добавляем 2 карты в прикуп
        for(int i=0; i<2; i++){
            history.setHistoryOfStore(cr.getCardFromDeck());
        }
        // сортируем карты у ботов
        westBot.sortingCards();
        eastBot.sortingCards();
        southBot.sortingCards();
        ArrayList<Card> a = southBot.getDeck();
        for(Card cd: a){
            System.out.print(cd.getName() + " ");
        }
        System.out.println();
        a = westBot.getDeck();
        for(Card cd: a){
            System.out.print(cd.getName() + " ");
        }
        System.out.println();
        a = eastBot.getDeck();
        for(Card cd: a){
            System.out.print(cd.getName() + " ");
        }
    }

    //запуск игры
    public void playTheGame(){
        distribution();
        gamestat = Gamestat.IS_DEAL;
        Deal deal = new Deal();
        int voice = firsthand; // номер закачика контракта
        //идут торги
        while(gamestat == Gamestat.IS_DEAL){
            String bid;
            if(southBot.getBotStatus()) {
                bid = analysis.getBid(southBot.getDeck());
                deal.setContract(southBot, somebodyIsPassed, gamestat, bid);
            }
            voice++;
            if(westBot.getBotStatus()) {
                bid = analysis.getBid(southBot.getDeck());
                deal.setContract(westBot, somebodyIsPassed, gamestat, bid);
            }
            voice++;
            if(eastBot.getBotStatus()){
                bid = analysis.getBid(southBot.getDeck());
                deal.setContract(eastBot, somebodyIsPassed, gamestat, bid);
            }
            voice++;
            if(voice == 3) voice=1;

            if(!(southBot.getBotStatus()) & !(westBot.getBotStatus()) & !(eastBot.getBotStatus()))      //все спасовали
                gamestat = Gamestat.IS_ALL_PASS;
            if(  (southBot.getBotStatus()) & !(westBot.getBotStatus()) & !(eastBot.getBotStatus()) ||   //играет юг
                !(southBot.getBotStatus()) &   westBot.getBotStatus()  & !(eastBot.getBotStatus()) ||   //играет запад
                !(southBot.getBotStatus()) & !(westBot.getBotStatus()) &   eastBot.getBotStatus()       //играет восток
              )
                gamestat = Gamestat.IS_PLAY;
        }
        for(String str: deal.getContractHistory()){
            System.out.println(str);
        }
        //идет игра
        ArrayList<String> contract = concludeTheFinalContract(deal);
        voice = firsthand;
        ArrayList<Card> moves = new ArrayList<>();
        Card cd;
<<<<<<< HEAD
        //если два игрока не вистуют - конец игры
        if(!southBot.getBotStatus() & !westBot.getBotStatus() ||
                !southBot.getBotStatus() & !eastBot.getBotStatus() ||
                !westBot.getBotStatus() & !eastBot.getBotStatus()){
            gamestat = Gamestat.IS_END;
        }
        //если хотя бы один вистует
        while(gamestat == Gamestat.IS_PLAY){
=======
/*        while(gamestat == Gamestat.IS_PLAY){
            if(!southBot.getBotStatus() & !westBot.getBotStatus() ||
               !southBot.getBotStatus() & !eastBot.getBotStatus() ||
               !westBot.getBotStatus() & !eastBot.getBotStatus()){

            }
            else{
>>>>>>> 8123c1df33d288c76eede75db24905e4c3b1fdff

            switch (voice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            voice++;
            if(voice == 3) voice =1;
        }
        while(gamestat == Gamestat.IS_ALL_PASS){

        }*/
    }


    //объявление окончательного контракта
    private ArrayList<String> concludeTheFinalContract(Deal deal){
        ArrayList<String> contract= new ArrayList<>();
        if(southBot.getBotStatus()) {
            contract = getContract(southBot);
        }
        if(westBot.getBotStatus()) {
            contract = getContract(westBot);
        }
        if(eastBot.getBotStatus()) {
            contract = getContract(eastBot);
        }
        return contract;
    }

    private ArrayList<String> getContract(Bot bot){
        for(Card card : history.getHistoryOfStore()){
            bot.addCard(card);
        }
        bot.sortingCards();
        return analysis.getFinalContract(bot.getDeck(), history);

    }
}
