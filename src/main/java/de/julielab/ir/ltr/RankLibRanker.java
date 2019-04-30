package de.julielab.ir.ltr;

import cc.mallet.types.FeatureVector;
import ciir.umass.edu.learning.*;
import ciir.umass.edu.metric.METRIC;
import ciir.umass.edu.metric.MetricScorerFactory;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RankLibRanker implements Ranker {

    private final MetricScorerFactory metricScorerFactory;
    private ciir.umass.edu.learning.Ranker ranker;

    public RankLibRanker() {
        metricScorerFactory = new MetricScorerFactory();
    }

    @Override
    public void train(DocumentList documents) {
        final LinkedHashMap<String, List<DataPoint>> dataPointsByQueryId = documents.stream().map(d -> {
            final FeatureVector fv = d.getFeatureVector();
            final double[] values = fv.getValues();
            final int[] indices = fv.getIndices();

            // In RankLib, the 0 index is unused.
            float[] ranklibValues = new float[indices.length + 1];
            int[] ranklibIndices = new int[indices.length + 1];
            if (values == null) {
                // binary vector
                Arrays.fill(ranklibValues, 1f);
            } else {
                System.arraycopy(values, 0, ranklibValues, 1, values.length);
            }
            System.arraycopy(indices, 0, ranklibIndices, 1, indices.length);
            return new SparseDataPoint(ranklibValues, ranklibIndices, d.getId(), d.getRelevance());
        }).collect(Collectors.groupingBy(dp -> dp.getID(), LinkedHashMap::new, Collectors.toList()));
        final LinkedHashMap<String, RankList> rankLists = new LinkedHashMap<>();
        dataPointsByQueryId.forEach((key,value) -> rankLists.put(key, new RankList(value)));
        ranker = new RankerFactory().createRanker(RANKER_TYPE.COOR_ASCENT, new ArrayList(rankLists.values()), null, metricScorerFactory.createScorer(METRIC.NDCG, 20));
    }

    @Override
    public void load(File modelFile) {

    }

    @Override
    public void save(File modelFile) {

    }

    @Override
    public DocumentList rank(DocumentList documents) {
        return null;
    }


}
