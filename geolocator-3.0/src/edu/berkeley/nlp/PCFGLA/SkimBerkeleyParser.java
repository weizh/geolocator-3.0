package edu.berkeley.nlp.PCFGLA;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import edu.berkeley.nlp.io.PTBLineLexer;
import edu.berkeley.nlp.syntax.Tree;
import edu.berkeley.nlp.ui.TreeJPanel;
import edu.berkeley.nlp.util.Numberer;

/**
 * Reads in the Penn Treebank and generates N_GRAMMARS different grammars.
 * 
 * @author Slav Petrov
 */
public class SkimBerkeleyParser {
  static TreeJPanel tjp;

  static JFrame frame;

  public static class Options {

    @Option(name = "-gr", required = true, usage = "Grammarfile (Required)\n")
    public String grFileName;

    @Option(name = "-tokenize", usage = "Tokenize input first. (Default: false=text is already tokenized)")
    public boolean tokenize = false;

    @Option(name = "-viterbi", usage = "Compute viterbi derivation instead of max-rule tree (Default: max-rule)")
    public boolean viterbi;

    @Option(name = "-binarize", usage = "Output binarized trees. (Default: false)")
    public boolean binarize;

    @Option(name = "-scores", usage = "Output inside scores (only for binarized viterbi trees). (Default: false)")
    public boolean scores;

    @Option(name = "-keepFunctionLabels", usage = "Retain predicted function labels. Model must have been trained with function labels. (Default: false)")
    public boolean keepFunctionLabels;

    @Option(name = "-substates", usage = "Output subcategories (only for binarized viterbi trees). (Default: false)")
    public boolean substates;

    @Option(name = "-accurate", usage = "Set thresholds for accuracy. (Default: set thresholds for efficiency)")
    public boolean accurate;

    @Option(name = "-modelScore", usage = "Output effective model score (max rule score for max rule parser) (Default: false)")
    public boolean modelScore;

    @Option(name = "-confidence", usage = "Output confidence measure, i.e. likelihood of tree given words: P(T|w) (Default: false)")
    public boolean confidence;

    @Option(name = "-sentence_likelihood", usage = "Output sentence likelihood, i.e. summing out all parse trees: P(w) (Default: false)")
    public boolean sentence_likelihood;

    @Option(name = "-tree_likelihood", usage = "Output joint likelihood of tree and words: P(t,w) (Default: false)")
    public boolean tree_likelihood;

    @Option(name = "-variational", usage = "Use variational rule score approximation instead of max-rule (Default: false)")
    public boolean variational;

    @Option(name = "-render", usage = "Write rendered tree to image file. (Default: false)")
    public boolean render;

    @Option(name = "-chinese", usage = "Enable some Chinese specific features in the lexicon.")
    public boolean chinese;

    @Option(name = "-inputFile", usage = "Read input from this file instead of reading it from STDIN.")
    public String inputFile;

    @Option(name = "-maxLength", usage = "Maximum sentence length (Default = 200).")
    public int maxLength = 200;

    @Option(name = "-nThreads", usage = "Parse in parallel using n threads (Default: 1).")
    public int nThreads = 1;

    @Option(name = "-kbest", usage = "Output the k best parse max-rule trees (Default: 1).")
    public int kbest = 1;

    @Option(name = "-outputFile", usage = "Store output in this file instead of printing it to STDOUT.")
    public String outputFile;

    @Option(name = "-useGoldPOS", usage = "Read data in CoNLL format, including gold part of speech tags.")
    public boolean goldPOS;

    @Option(name = "-dumpPosteriors", usage = "Dump max-rule posteriors to disk.")
    public boolean dumpPosteriors;

    @Option(name = "-ec_format", usage = "Use Eugene Charniak's input and output format.")
    public boolean ec_format;

    @Option(name = "-nGrammars", usage = "Use a product model based on that many grammars")
    public int nGrammars = 1;
  }

  static OptionParser optParser;

  static Options opts;

  static ParserData pData;

  public static void initialize(String args[]) {
    OptionParser optParser = new OptionParser(Options.class);
    Options opts = (Options) optParser.parse(args, true);
    String inFileName = opts.grFileName;
    pData = ParserData.Load(inFileName);
    if (pData == null) {
      System.err.println("Failed to load grammar from file " + inFileName);
    }

  };

  @SuppressWarnings("unchecked")
  public static List<Tree<String>> parse(String line) {

    List<Tree<String>> parsedTrees = null;

    double threshold = 1.0;

    CoarseToFineMaxRuleParser parser = null;

    Grammar grammar = pData.getGrammar();
    Lexicon lexicon = pData.getLexicon();
    Numberer.setNumberers(pData.getNumbs());

    parser = new CoarseToFineMaxRuleParser(grammar, lexicon, threshold, -1, opts.viterbi,
            opts.substates, opts.scores, opts.accurate, opts.variational, true, true);

    parser.binarization = pData.getBinarization();

    MultiThreadedParserWrapper m_parser = null;

    try {

      PTBLineLexer tokenizer = null;
      tokenizer = new PTBLineLexer();

      line = line.trim();
      if (opts.ec_format && line.equals(""))
        return null;
      List<String> sentence = null;
      List<String> posTags = null;
      sentence = tokenizer.tokenizeLine(line);

      parsedTrees = new ArrayList<Tree<String>>();
      Tree<String> parsedTree = parser.getBestConstrainedParse(sentence, posTags, null);

      parsedTrees.add(parsedTree);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return parsedTrees;
  }

  /**
   * @param parsedTree
   * @param outputData
   * @param opts
   */
  private static void outputTrees(List<Tree<String>> parseTrees, PrintWriter outputData,
          CoarseToFineMaxRuleParser parser, edu.berkeley.nlp.PCFGLA.BerkeleyParser.Options opts,
          String line, String sentenceID) {
    String delimiter = "\t";
    if (opts.ec_format) {
      List<Tree<String>> newList = new ArrayList<Tree<String>>(parseTrees.size());
      for (Tree<String> parsedTree : parseTrees) {
        if (parsedTree.getChildren().isEmpty())
          continue;
        if (parser.getLogLikelihood(parsedTree) != Double.NEGATIVE_INFINITY) {
          newList.add(parsedTree);
        }
      }
      parseTrees = newList;
    }
    if (opts.ec_format) {
      outputData.write(parseTrees.size() + "\t" + sentenceID + "\n");
      delimiter = ",\t";
    }

    for (Tree<String> parsedTree : parseTrees) {
      boolean addDelimiter = false;
      if (opts.tree_likelihood) {
        double treeLL = (parsedTree.getChildren().isEmpty()) ? Double.NEGATIVE_INFINITY : parser
                .getLogLikelihood(parsedTree);
        if (treeLL == Double.NEGATIVE_INFINITY)
          continue;
        outputData.write(treeLL + "");
        addDelimiter = true;
      }
      if (opts.sentence_likelihood) {
        double allLL = (parsedTree.getChildren().isEmpty()) ? Double.NEGATIVE_INFINITY : parser
                .getLogLikelihood();
        if (addDelimiter)
          outputData.write(delimiter);
        addDelimiter = true;
        if (opts.ec_format)
          outputData.write("sentenceLikelihood ");
        outputData.write(allLL + "");
      }
      if (!opts.binarize)
        parsedTree = TreeAnnotations.unAnnotateTree(parsedTree, opts.keepFunctionLabels);
      if (opts.confidence) {
        double treeLL = (parsedTree.getChildren().isEmpty()) ? Double.NEGATIVE_INFINITY : parser
                .getConfidence(parsedTree);
        if (addDelimiter)
          outputData.write(delimiter);
        addDelimiter = true;
        if (opts.ec_format)
          outputData.write("confidence ");
        outputData.write(treeLL + "");
      } else if (opts.modelScore) {
        double score = (parsedTree.getChildren().isEmpty()) ? Double.NEGATIVE_INFINITY : parser
                .getModelScore(parsedTree);
        if (addDelimiter)
          outputData.write(delimiter);
        addDelimiter = true;
        if (opts.ec_format)
          outputData.write("maxRuleScore ");
        outputData.write(String.format("%.8f", score));
      }
      if (opts.ec_format)
        outputData.write("\n");
      else if (addDelimiter)
        outputData.write(delimiter);
      if (!parsedTree.getChildren().isEmpty()) {
        String treeString = parsedTree.getChildren().get(0).toString();
        if (parsedTree.getChildren().size() != 1) {
          System.err.println("ROOT has more than one child!");
          parsedTree.setLabel("");
          treeString = parsedTree.toString();
        }
        if (opts.ec_format)
          outputData.write("(S1 " + treeString + " )\n");
        else
          outputData.write("( " + treeString + " )\n");
      } else {
        outputData.write("(())\n");
      }
      if (opts.render)
        try {
          writeTreeToImage(parsedTree, line.replaceAll("[^a-zA-Z]", "") + ".png");
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    if (opts.dumpPosteriors) {
      int blockSize = 50;
      String fileName = opts.grFileName + ".posteriors";
      parser.dumpPosteriors(fileName, blockSize);
    }

    if (opts.kbest > 1)
      outputData.write("\n");
    outputData.flush();

  }

  public static void writeTreeToImage(Tree<String> tree, String fileName) throws IOException {
    if (tjp == null)
      tjp = new TreeJPanel();
    tjp.setTree(tree);

    BufferedImage bi = new BufferedImage(tjp.width(), tjp.height(), BufferedImage.TYPE_INT_ARGB);
    int t = tjp.height();
    Graphics2D g2 = bi.createGraphics();

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1.0f));
    Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, tjp.width(), tjp.height());
    g2.fill(rect);

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    tjp.paintComponent(g2); // paint the graphic to the offscreen image
    g2.dispose();

    ImageIO.write(bi, "png", new File(fileName)); // save as png format
    // DONE!
  }

}
