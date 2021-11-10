package advent;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Day19 {

    private final static Pattern REGEX_RULE = Pattern.compile("^([0-9]+): (.*)$");

    private final static Pattern REGEX_SENTENCE = Pattern.compile("^([ab]+)$");

    public static void main(String[] args) throws IOException {
        System.out.println("\n--- Day 19: Monster Messages ---");

        BufferedReader reader = Files.newBufferedReader(Paths.get("day19sample.txt"));
        List<String> lines = reader.lines().collect(Collectors.toList());
        reader.close();

        HashMap<Integer, Rule> rules = new HashMap<>();

        lines.stream().forEach(x -> {
            Matcher matcher = REGEX_RULE.matcher(x.replace("\"", ""));
            if (matcher.matches()) {
                int number = Integer.parseInt(matcher.group(1));
                String[] values = matcher.group(2).split(" \\| ");

                ArrayList<Integer> left = new ArrayList<>();
                if (values[0].equals("a") || values[0].equals("b")) {
                    rules.put(number, new Rule(values[0]));
                } else {
                    left.addAll(stream(values[0].split(" ")).map(Integer::parseInt).collect(Collectors.toList()));

                    ArrayList<Integer> right = new ArrayList<>();
                    if (values.length > 1) {
                        right.addAll(stream(values[1].split(" ")).map(Integer::parseInt).collect(Collectors.toList()));
                    }

                    rules.put(number, new Rule(left, right));
                }
            }
        });

        List<String> sentences = lines.stream().filter(x -> REGEX_SENTENCE.matcher(x).matches()).collect(Collectors.toList());

        Pattern regex = Pattern.compile(rules.get(0).getRegex(rules, -1, 0));
        System.out.println("Part 1: " + sentences.parallelStream().filter(x -> regex.matcher(x).matches()).count());

        rules.put(8, new Rule(new ArrayList<>(asList(42)), new ArrayList<>(asList(42, 8))));
        rules.put(11, new Rule(new ArrayList<>(asList(42, 31)), new ArrayList<>(asList(42, 11, 31))));

        Pattern regex2 = Pattern.compile(rules.get(0).getRegex(rules, -1, 0));
        System.out.println("Part 2: " + sentences.parallelStream().filter(x -> regex2.matcher(x).matches()).count());
    }

//    public static void main(String[] args) throws IOException {
//        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day19sample.txt"))) {
//            System.out.println(part1(bufferedReader));
//        }
//    }

    static Pattern TERMINATE_CHAR = Pattern.compile("\"(.*?)\"");
    static Pattern N_RECURSION = Pattern.compile("\\{\\d+\\}");
    static Pattern RULE_N_RECURSION = Pattern.compile("\\d+\\{\\d+\\}");

    static class Rule {
        private ArrayList<Integer> left;

        private ArrayList<Integer> right;

        private String value;

        private int alreadyCalledItself;

        public Rule(ArrayList<Integer> left, ArrayList<Integer> right) {
            this.left = left;
            this.right = right;
        }

        public Rule(String value) {
            this.value = value;
            this.left = new ArrayList<>();
            this.right = new ArrayList<>();
        }

        public String getRegex(HashMap<Integer, Rule> rules, int previous, int number) {
            if (value != null) {
                return value;
            }

            String regex = "(";
            for (Integer rule : left) {
                regex += rules.get(rule).getRegex(rules, number, rule);
            }
            if (!right.isEmpty()) {
                regex += "|";
                for (Integer rule : right) {
                    if (number == rule) {
                        if (previous == number) {
                            alreadyCalledItself++;
                        } else {
                            alreadyCalledItself = 0;
                        }

                        if (alreadyCalledItself <= 1) {
                            regex += rules.get(rule).getRegex(rules, number, rule);
                        } else {
                            regex += ".+";
                        }
                    } else {
                        regex += rules.get(rule).getRegex(rules, number, rule);
                    }
                }
            }

            return regex + ")";
        }
    }


    static class Message {
        final String mText;
        final Predicate<String> stringPredicate;
        final boolean isPattern;
        final Pattern pattern;

        public Message(String message) {
            this.mText = message;
            stringPredicate = message::equals;
            isPattern = false;
            pattern = null;
        }

        public Message(String mText, Pattern pattern) {
            this.mText = mText;
            stringPredicate = s -> pattern.matcher(s).matches();
            isPattern = true;
            this.pattern = pattern;
        }

        public Message(String mText, boolean isPattern) {
            this.mText = mText;
            stringPredicate = null;
            this.isPattern = isPattern;
            pattern = null;
        }

        public boolean match(String m) {
            return stringPredicate.test(m);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Message)) {
                return false;
            }
            Message message = (Message) o;
            return Objects.equal(mText, message.mText);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(mText);
        }
    }

    static class RuleMessage {
        final String rule;
        Set<RuleMessageSeq> ruleMessageSeqs = new HashSet<>();
        Set<RuleMessage> usedIn = new HashSet<>();

        boolean built = false;
        Set<Message> messages = new LinkedHashSet<>();

        public RuleMessage(String rule) {
            this.rule = rule;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RuleMessage)) {
                return false;
            }
            RuleMessage that = (RuleMessage) o;
            return Objects.equal(rule, that.rule);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(rule);
        }

        public void rebuild() {
            if (ruleMessageSeqs.isEmpty() || built) {
                return;
            }

            boolean allBuilt = true;

            for (RuleMessageSeq ruleMessageSeq : ruleMessageSeqs) {
                ruleMessageSeq.rebuild();
                allBuilt = allBuilt && ruleMessageSeq.built;
            }

            built = allBuilt;

            if (built) {
                messages = ruleMessageSeqs.stream().flatMap(ruleMessageSeq -> ruleMessageSeq.messages.stream()).collect(Collectors.toSet());
                usedIn.forEach(RuleMessage::rebuild);
            }
        }
    }

    static class RuleMessageSeq {
        private static final int DEPTH = 4;
        final List<RuleMessage> seq = new ArrayList<>();
        final Map<Integer, String> recursiveRuleIndices = new HashMap<>();
        boolean built;
        final String originalSeq;
        Set<Message> messages = new LinkedHashSet<>();

        public RuleMessageSeq(String seq) {
            this.originalSeq = " " + seq.replace("\"", "") + " ";
            final Matcher matcher = TERMINATE_CHAR.matcher(seq);
            built = matcher.matches();
            if (built) {
                messages.add(new Message(matcher.group(0).replace("\"", "")));
            }
        }

        private void add(RuleMessage ruleMessage, boolean recursive, String recursionType) {
            seq.add(ruleMessage);
            if (recursive) {
                this.recursiveRuleIndices.put(seq.size() - 1, recursionType);
            }

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RuleMessageSeq)) {
                return false;
            }
            RuleMessageSeq that = (RuleMessageSeq) o;
            return Objects.equal(originalSeq, that.originalSeq);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(originalSeq);
        }

        public void rebuild() {
            if (built) {
                return;
            }

            boolean allBuilt = true;

            for (RuleMessage ruleMessage : seq) {
                ruleMessage.rebuild();
                allBuilt = allBuilt && ruleMessage.built;
            }

            if (allBuilt) {
                List<List<Message>> lists = new ArrayList<>();
                for (int i = 0; i < seq.size(); i++) {
                    RuleMessage ruleMessage = seq.get(i);
                    final String recursionType = recursiveRuleIndices.get(i);

                    List<Message> messageArrayList = new ArrayList<>(ruleMessage.messages.size());
                    for (Message message : ruleMessage.messages) {
                        Message message1 = message;
                        if (recursionType != null) {
                            final String regex = "(" + message.mText + ")"+recursionType;
                            message1 = new Message(regex, true);
                        }
                        messageArrayList.add(message1);
                    }

                    lists.add(messageArrayList);
                }

                final List<List<Message>> cartesianProduct = Lists.cartesianProduct(lists);
                messages = new LinkedHashSet<>();
                for (final List<Message> strings : cartesianProduct) {
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean isPattern = false;
                    for (Message m : strings) {
                        isPattern = isPattern || m.isPattern;
                        stringBuilder.append(m.mText);
                    }
                    final Message newMessage;
                    final String mText = stringBuilder.toString().replace(" ", "");
                    if (isPattern) {
                        stringBuilder = new StringBuilder();
                        if (mText.contains("{x}")) {
                            stringBuilder.append("(");
                            for (int n = 1; n <= DEPTH; n++) {
                                stringBuilder.append("(").append(mText.replaceAll("\\{x\\}", "{" + n + "}")).append(")");
                                if (n < DEPTH) {
                                    stringBuilder.append("|");
                                }
                            }
                            stringBuilder.append(")+");
                        } else {
                            stringBuilder.append(mText);
                        }

                        final String mText1 = stringBuilder.toString();
                        newMessage = new Message(mText1, Pattern.compile(mText1));
                    } else {
                        newMessage = new Message(mText);
                    }
                    messages.add(newMessage);
                }
            }
            built = allBuilt;
        }

    }

    private static long part1(BufferedReader bufferedReader) throws IOException {
        String line;
        long count = 0;
        boolean rules = true;
        final Map<String, RuleMessage> ruleMessages = new HashMap<>();
        final Map<String, RuleMessageSeq> messageSequences = new HashMap<>();

        while ((line = bufferedReader.readLine()) != null) {

            if (line.length() == 0) {
                rules = false;
                continue;
            }

            if (rules) {
                final String[] split = line.split(": ");
                String rule = split[0];
                String message = split[1];

                final RuleMessage ruleMessage = ruleMessages.computeIfAbsent(rule, r -> new RuleMessage(rule));

                final String[] seqs = message.split(Pattern.quote(" | "));
                for (String seq : seqs) {
                    buildMessageSeq(seq, ruleMessage, ruleMessages, messageSequences);
                }

                ruleMessage.rebuild();
                ruleMessages.putIfAbsent(rule, ruleMessage);
            } else {
                for (Message message : ruleMessages.get("0").messages) {
                    System.out.println(message.mText);

                    if (message.match(line)) {
                        count++;
                        break;
                    }
                }

            }
        }
        return count;
    }

    private static void buildMessageSeq(String seq,
                                        RuleMessage usedIn,
                                        Map<String, RuleMessage> ruleMessages,
                                        Map<String, RuleMessageSeq> messageSequences) {

        final String[] rules = seq.split(" ");
        RuleMessageSeq ruleMessageSeq = messageSequences.get(seq);
        final boolean newSeq = ruleMessageSeq == null;
        if (newSeq) {
            ruleMessageSeq = new RuleMessageSeq(seq);
            messageSequences.put(seq, ruleMessageSeq);
        }

        for (String rule : rules) {
            final String ruleNumber;
            final boolean plusRecursion = rule.contains("+");
            final boolean finiteRecursion = rule.contains("{x}");
            boolean recursive = plusRecursion || finiteRecursion;

            String recursionType = null;
            if (plusRecursion) {
                recursionType = "+";
            }

            if (finiteRecursion) {
                recursionType = "{x}";
            }

            if (recursive) {
                if (finiteRecursion) {
                    ruleNumber = rule.replace("{x}", "");
                } else {
                    ruleNumber = rule.replace("+", "");
                }
            } else {
                ruleNumber = rule;
            }
            if (StringUtils.isNumeric(ruleNumber)) {
                final RuleMessage ruleMessage = ruleMessages.computeIfAbsent(ruleNumber, r -> new RuleMessage(ruleNumber));
                ruleMessage.usedIn.add(usedIn);
                if (newSeq) {
                    ruleMessageSeq.add(ruleMessage, recursive, recursionType);
                }
            }
            usedIn.ruleMessageSeqs.add(ruleMessageSeq);
        }
    }


}
