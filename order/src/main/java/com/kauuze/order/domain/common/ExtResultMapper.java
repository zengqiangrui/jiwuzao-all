package com.kauuze.order.domain.common;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.AbstractResultMapper;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.PersistentPropertyPaths;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.context.InvalidPersistentPropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;


/**
 * 用于elasticsearch结果映射
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-27 13:27
 */
@Component
public class ExtResultMapper extends AbstractResultMapper {

    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public ExtResultMapper() {
        super(new DefaultEntityMapper(new MappingContext<ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty>() {
            @Override
            public Collection<ElasticsearchPersistentEntity<?>> getPersistentEntities() {
                return null;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(Class<?> type) {
                return null;
            }

            @Override
            public boolean hasPersistentEntityFor(Class<?> type) {
                return false;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(TypeInformation<?> type) {
                return null;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(ElasticsearchPersistentProperty persistentProperty) {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(PropertyPath propertyPath) throws InvalidPersistentPropertyPath {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(String propertyPath, Class<?> type) throws InvalidPersistentPropertyPath {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(InvalidPersistentPropertyPath invalidPath) {
                return null;
            }

            @Override
            public <T> PersistentPropertyPaths<T, ElasticsearchPersistentProperty> findPersistentPropertyPaths(Class<T> type, Predicate<? super ElasticsearchPersistentProperty> predicate) {
                return null;
            }

            @Override
            public Collection<TypeInformation<?>> getManagedTypes() {
                return null;
            }
        }));
    }

    public ExtResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(new DefaultEntityMapper(new MappingContext<ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty>() {
            @Override
            public Collection<ElasticsearchPersistentEntity<?>> getPersistentEntities() {
                return null;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(Class<?> type) {
                return null;
            }

            @Override
            public boolean hasPersistentEntityFor(Class<?> type) {
                return false;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(TypeInformation<?> type) {
                return null;
            }

            @Nullable
            @Override
            public ElasticsearchPersistentEntity<?> getPersistentEntity(ElasticsearchPersistentProperty persistentProperty) {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(PropertyPath propertyPath) throws InvalidPersistentPropertyPath {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(String propertyPath, Class<?> type) throws InvalidPersistentPropertyPath {
                return null;
            }

            @Override
            public PersistentPropertyPath<ElasticsearchPersistentProperty> getPersistentPropertyPath(InvalidPersistentPropertyPath invalidPath) {
                return null;
            }

            @Override
            public <T> PersistentPropertyPaths<T, ElasticsearchPersistentProperty> findPersistentPropertyPaths(Class<T> type, Predicate<? super ElasticsearchPersistentProperty> predicate) {
                return null;
            }

            @Override
            public Collection<TypeInformation<?>> getManagedTypes() {
                return null;
            }
        }));
        this.mappingContext = mappingContext;
    }

    public ExtResultMapper(EntityMapper entityMapper) {
        super(entityMapper);
    }

    public ExtResultMapper(
            MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext,
            EntityMapper entityMapper) {
        super(entityMapper);
        this.mappingContext = mappingContext;
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        long totalHits = response.getHits().getTotalHits();
        List<T> results = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            if (hit != null) {
                T result = null;
                if (StringUtils.hasText(hit.getSourceAsString())) {
                    result = mapEntity(hit.getSourceAsString(), clazz);
                } else {
                    result = mapEntity(hit.getFields().values(), clazz);
                }
                setPersistentEntityId(result, hit.getId(), clazz);
                setPersistentEntityVersion(result, hit.getVersion(), clazz);
                populateScriptFields(result, hit);
               
               // 高亮查询
                populateHighLightedFields(result, hit.getHighlightFields());
                results.add(result);
            }
        }

        return new AggregatedPageImpl<T>(results, pageable, totalHits, response.getAggregations(), response.getScrollId());
    }

    private <T>  void populateHighLightedFields(T result, Map<String, HighlightField> highlightFields) {
        for (HighlightField field : highlightFields.values()) {
            try {
                PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new ElasticsearchException("failed to set highlighted value for field: " + field.getName()
                        + " with value: " + Arrays.toString(field.getFragments()), e);
            }
        }
    }

    private String concat(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }

    private <T> void populateScriptFields(T result, SearchHit hit) {
        if (hit.getFields() != null && !hit.getFields().isEmpty() && result != null) {
            for (java.lang.reflect.Field field : result.getClass().getDeclaredFields()) {
                ScriptedField scriptedField = field.getAnnotation(ScriptedField.class);
                if (scriptedField != null) {
                    String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
                    DocumentField searchHitField = hit.getFields().get(name);
                    if (searchHitField != null) {
                        field.setAccessible(true);
                        try {
                            field.set(result, searchHitField.getValue());
                        } catch (IllegalArgumentException e) {
                            throw new ElasticsearchException("failed to set scripted field: " + name + " with value: "
                                    + searchHitField.getValue(), e);
                        } catch (IllegalAccessException e) {
                            throw new ElasticsearchException("failed to access scripted field: " + name, e);
                        }
                    }
                }
            }
        }
    }

    private <T> T mapEntity(Collection<DocumentField> values, Class<T> clazz) {
        return mapEntity(buildJSONFromFields(values), clazz);
    }

    private String buildJSONFromFields(Collection<DocumentField> values) {
        JsonFactory nodeFactory = new JsonFactory();
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            for (DocumentField value : values) {
                if (value.getValues().size() > 1) {
                    generator.writeArrayFieldStart(value.getName());
                    for (Object val : value.getValues()) {
                        generator.writeObject(val);
                    }
                    generator.writeEndArray();
                } else {
                    generator.writeObjectField(value.getName(), value.getValue());
                }
            }
            generator.writeEndObject();
            generator.flush();
            return new String(stream.toByteArray(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public <T> T mapResult(GetResponse response, Class<T> clazz) {
        T result = mapEntity(response.getSourceAsString(), clazz);
        if (result != null) {
            setPersistentEntityId(result, response.getId(), clazz);
            setPersistentEntityVersion(result, response.getVersion(), clazz);
        }
        return result;
    }

    @Override
    public <T> LinkedList<T> mapResults(MultiGetResponse responses, Class<T> clazz) {
        LinkedList<T> list = new LinkedList<>();
        for (MultiGetItemResponse response : responses.getResponses()) {
            if (!response.isFailed() && response.getResponse().isExists()) {
                T result = mapEntity(response.getResponse().getSourceAsString(), clazz);
                setPersistentEntityId(result, response.getResponse().getId(), clazz);
                setPersistentEntityVersion(result, response.getResponse().getVersion(), clazz);
                list.add(result);
            }
        }
        return list;
    }

    private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {

        if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {

            ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getRequiredPersistentEntity(clazz);
            ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();

            // Only deal with String because ES generated Ids are strings !
            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
            }

        }
    }

    private <T> void setPersistentEntityVersion(T result, long version, Class<T> clazz) {
        if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {

            ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(clazz);
            ElasticsearchPersistentProperty versionProperty = persistentEntity.getVersionProperty();

            // Only deal with Long because ES versions are longs !
            if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
                // check that a currentVersion was actually returned in the response, -1 would indicate that
                // a search didn't request the currentVersion ids in the response, which would be an issue
                Assert.isTrue(version != -1, "Version in response is -1");
                persistentEntity.getPropertyAccessor(result).setProperty(versionProperty, version);
            }
        }
    }
}