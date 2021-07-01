package com.backbase.config.init;

import com.backbase.model.entity.Movie;
import com.backbase.model.service.MovieService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVImporter {
    private static final Logger logger = LoggerFactory.getLogger(CSVImporter.class);

    private MovieService movieService;

    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }


    @Value("${sample.data.csvPath:classpath:academy_awards.csv}")
    private Resource csvSampleData;

    @Value("${sample.data.batchSize:100}")
    private int batchSize;

    private enum HeaderTitle {
        YEAR("Year"),
        CATEGORY("Category"),
        NOMINEE("Nominee"),
        ADDITIONAL_INFO("Additional Info"),
        WON("Won?");

        private String caption;

        HeaderTitle(String caption) {
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }
    }

    private String[] headerTitles() {
        HeaderTitle[] headerEnums = HeaderTitle.values();
        String[] headerCaptions = new String[headerEnums.length];
        for (int i = 0; i < headerCaptions.length; i++) {
            headerCaptions[i] = headerEnums[i].getCaption();
        }
        return headerCaptions;
    }

    void importData() {
        if (csvSampleData != null && csvSampleData.isReadable()) {
            try {
                logger.info("Try to import data from " + csvSampleData.getURL().toString());

                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withAllowMissingColumnNames()
                        .withHeader(headerTitles())
                        .withFirstRecordAsHeader()
                        .withTrim()
                        .parse(new InputStreamReader(csvSampleData.getInputStream(), StandardCharsets.UTF_8));
                List<Movie> batchList = new ArrayList<>();
                for (CSVRecord record : records) {
                    String category = record.get(HeaderTitle.CATEGORY.getCaption()).trim();
                    if ("Best Picture".equalsIgnoreCase(category)) {
                        String won = record.get(HeaderTitle.WON.getCaption());
                        String year = record.get(HeaderTitle.YEAR.getCaption()).trim();
                        String title = record.get(HeaderTitle.NOMINEE.getCaption()).trim();
                        String additionInfo = record.get(HeaderTitle.ADDITIONAL_INFO.getCaption()).trim();
                        batchList.add(new Movie(year, title, additionInfo, won));
                    }
                    if (batchSize == batchList.size()) {
                        logger.info("import data from csv, batch size: " + batchSize);
                        movieService.saveMovie(batchList);
                        batchList.clear();
                    }
                }

                if (batchList.size() > 0) {
                    logger.info("import data from csv, last batch size: " + batchSize);
                    movieService.saveMovie(batchList);
                    batchList.clear();
                }

            } catch (Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            logger.error("Csv resource '" + csvSampleData + "' is not valid path or is not readable");
        }
    }

}
