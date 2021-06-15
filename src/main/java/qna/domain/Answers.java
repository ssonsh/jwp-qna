package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers implements Iterable<Answer> {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }
    public Answers() { this(new ArrayList<>()); }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> histories = new ArrayList<>();
        for (Answer answer: answersWithDeletedFalse()) {
            histories.add(answer.delete(loginUser));
        }
        return histories;
    }

    private List<Answer> answersWithDeletedFalse() {
        return answers.stream()
                .filter(answer->!answer.isDeleted())
                .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    @Override
    public Iterator<Answer> iterator() {
        return answers.iterator();
    }
}