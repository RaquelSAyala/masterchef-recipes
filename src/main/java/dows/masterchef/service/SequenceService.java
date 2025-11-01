package dows.masterchef.service;
import dows.masterchef.model.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
@Service
public class SequenceService {
    private final MongoOperations mongo;
    @Autowired
    public SequenceService(MongoOperations mongo) { this.mongo = mongo; }
    public long getNextSequence(String name) {
        Query query = new Query(Criteria.where("_id").is(name));
        Update update = new Update();
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        Counter counter = mongo.findAndModify(query, update, options, Counter.class);
        return counter.getSeq();
    }
}
