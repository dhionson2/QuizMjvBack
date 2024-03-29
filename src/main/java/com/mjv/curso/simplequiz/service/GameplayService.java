package com.mjv.curso.simplequiz.service;

import com.mjv.curso.simplequiz.builder.PlayerMapper;
import com.mjv.curso.simplequiz.builder.QuestionAlternativeMapper;
import com.mjv.curso.simplequiz.builder.QuestionMapper;
import com.mjv.curso.simplequiz.dto.PlayerDTO;
import com.mjv.curso.simplequiz.dto.QuestionDTO;
import com.mjv.curso.simplequiz.dto.UserDTO;
import com.mjv.curso.simplequiz.model.GameplaySession;
import com.mjv.curso.simplequiz.model.Question;
import com.mjv.curso.simplequiz.model.QuestionAlternative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GameplayService {

    @Autowired
    private QuizDataBaseService quizDataBaseService;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionAlternativeMapper questionAlternativeMapper;

    private GameplaySession gameplaySession = new GameplaySession();



    public PlayerDTO startQuizGameplay(String nickName, String theme, UserDTO userDTO) {
        PlayerDTO playerDTO = new PlayerDTO(nickName, 0, theme, userDTO);

        //No fuguro pegará do banco de dados então retorna model e não dto.
        ArrayList<Question> questionsList = quizDataBaseService.showAllQuestions();
        ArrayList<Question> filteredQuestionsList = quizDataBaseService.findByTheme(questionsList, playerDTO.getTheme());

       // playerDTO.setPlayerQuestionsDTOList(questionMapper.toListDTO(filteredQuestionsList));
        return playerDTO;
    }


   /* public PlayerDTO validateItPlayerQuestionIsCorrect(PlayerDTO playerDTO) {
        System.out.println("Quantidade atual de perguntas no quiz para o playerDTO: " + playerDTO.getPlayerQuestionsDTOList().size());
        System.out.println("Usando o tema: " + playerDTO.getTheme());

        Long playerSelectedQuestionID = playerDTO.getCurrentQuestionID();

        //Recuperando dados em suposto banco de dados que ainda está em memória.
        Question currentPlayerQuestion = quizDataBaseService.findQuestionById(playerSelectedQuestionID);
        //Adicionando a questão recuperada em banco de dados dentro do objeto currentPlayerQuestion do userDTO.
        playerDTO.setCurrentPlayerQuestion(questionMapper.toDTO(currentPlayerQuestion));

        //Recuperando na base de dados todas as Questions por tema.
        List<Question> allPlayerQuestionsByTheme = quizDataBaseService.findByTheme(playerDTO.getTheme());

        //Iterator<QuestionDTO> questionsIntoPlayerIterator = questionMapper.toListDTO(allPlayerQuestionsByTheme).iterator();

        while (questionsIntoPlayerIterator.hasNext()) {
            QuestionDTO currentQuestionIntoIterator = questionsIntoPlayerIterator.next();
            Long questionsIntoPlayerIteratorId = currentQuestionIntoIterator.getId();

            //
            if (playerSelectedQuestionID.equals(questionsIntoPlayerIteratorId)) {
                System.out.println("Encontramos o mesmo id em playerSelectedQuestionID........= " + playerSelectedQuestionID);
                System.out.println("E encontramos o mesmo id em questionsIntoPlayerIteratorId = " + questionsIntoPlayerIteratorId);

                // Recuperamos a partir da Question atual a lista das alternativas da Question.
                ArrayList<QuestionAlternative> currentSelectedQuestionAlternativeList = quizDataBaseService.findQuestionAlternativesByQuestionId(playerSelectedQuestionID);

                System.out.println("Achamos as seguintes anternativas: " + currentSelectedQuestionAlternativeList);
                System.out.println("Quantidade de anternativas: " + currentSelectedQuestionAlternativeList.size());

                //Inserindo lista de alternativas na atual Question do Player
                playerDTO.getCurrentPlayerQuestionDTO().setQuestionAlternativeDTOArrayList(questionAlternativeMapper.toListDTO(currentSelectedQuestionAlternativeList));

                System.out.println("Nosso player agora tem uma question e alternativas: " + playerDTO);

                if(playerDTO.getSelectedQuestionAlternative().getItsCorrect()) {
                    //Insere Score se a alternativa estiver correta;
                    playerDTO.setScore(playerDTO.getScore() + 100);
                }
            }
        }
        return playerDTO;
    }*/


    public GameplaySession addPlayerToSession(PlayerDTO playerDTO){
        gameplaySession.getPlayerDTOList().add(playerDTO);
        return gameplaySession;
    }


    public GameplaySession getCurrentSession(){
        return gameplaySession;
    }


    public QuestionDTO findPlayerQuestionByID(PlayerDTO playerDTO, Long questionID){
        List<QuestionDTO> questionDTOList = playerDTO.getPlayerQuestionsDTOList();
        for(QuestionDTO questionDTO:questionDTOList){
            if(questionID.equals(questionDTO.getId())){
                return questionDTO;
            }
        }
        return null;
    }

    public QuestionDTO findPlayerQuestionAndAlternativesByQuestionId(Long questionID){
        QuestionDTO questionDTO = questionMapper.toDTO(quizDataBaseService.findQuestionById(questionID));

        List<QuestionAlternative> questionAlternativesList = quizDataBaseService.findQuestionAlternativesByQuestionId(questionID);
        questionDTO.setQuestionAlternativeDTOArrayList(questionAlternativeMapper.toListDTO(questionAlternativesList));
        return questionDTO;
    }

}
