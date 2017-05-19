package io.radicalbit.flink.pmml.java;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for the result of a JPMML evaluation. Offers extraction utilities to return
 * a subset of the output informations.
 */

public class EvaluationResult {

    private final Map<FieldName, Object> resultData;
    private final Evaluator evaluator;

    public EvaluationResult(Map<FieldName, Object> result_t, Evaluator evaluator_t) {
        resultData = result_t;
        evaluator = evaluator_t;
    }

    /**
     * @return A map containing only the target fields defined in the PMML.
     */
    public final Map<String, Object> getTargetFields() {
        final Map<String, Object> targetValues = new HashMap<>();
        for (TargetField targetField : evaluator.getTargetFields()) {
            if(targetField.getName() != null) {
                targetValues.put(targetField.getName().getValue(), EvaluatorUtil.decode(this.resultData.get(targetField.getName())));
            }
        }
        return targetValues;
    }

    /**
     * @return A map containing only the output fields defined in the PMML.
     */
    public final Map<String, Object> getOutputFields() {
        final Map<String, Object> outputValues = new HashMap<>();
        for (OutputField outputField : evaluator.getOutputFields()) {
            if(outputField.getName() != null) {
                outputValues.put(outputField.getName().getValue(), EvaluatorUtil.decode(this.resultData.get(outputField.getName())));
            }
        }
        return outputValues;
    }

    /**
     * @return A map containing the target and output fields defined in the PMML.
     */

    public Map<String, Object> getTargetAndOutputFields() {
        final Map<String, Object> t = getTargetFields();
        final Map<String, Object> o = getOutputFields();
        t.putAll(o);
        return t;
    }

    /**
     * @return A map containing the raw output objects from the JPMML evaluation.
     */

    public Map<String, Object> getRawResult() {

        final Map<String, Object> result = new HashMap<>();

        for (Map.Entry<FieldName, Object> entry : this.resultData.entrySet()) {
            String key;
            if (entry.getKey() == null)
                key = "";
            else
                key = entry.getKey().getValue();

            result.put(key, entry.getValue());
        }

        return result;

    }

    public Map<FieldName, Object> getResultData() {
        return resultData;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }
}