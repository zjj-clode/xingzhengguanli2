package com.cloudinte.common.utils;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * 支持5.x版本Lucene
 *
 * @author lsp
 *
 */
public class IKTokenizer5x extends Tokenizer {
	private IKSegmenter _IKImplement;
	private final CharTermAttribute termAtt = this.addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = this.addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = this.addAttribute(TypeAttribute.class);
	private int endPosition;

	public IKTokenizer5x() {
		this._IKImplement = new IKSegmenter(this.input, true);
	}

	public IKTokenizer5x(boolean useSmart) {
		this._IKImplement = new IKSegmenter(this.input, useSmart);
	}

	public IKTokenizer5x(AttributeFactory factory) {
		super(factory);
		this._IKImplement = new IKSegmenter(this.input, true);
	}

	@Override
	public boolean incrementToken() throws IOException {
		this.clearAttributes();
		Lexeme nextLexeme = this._IKImplement.next();
		if (nextLexeme != null) {
			this.termAtt.append(nextLexeme.getLexemeText());
			this.termAtt.setLength(nextLexeme.getLength());
			this.offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
			this.endPosition = nextLexeme.getEndPosition();
			this.typeAtt.setType(nextLexeme.getLexemeTypeString());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		this._IKImplement.reset(this.input);
	}

	@Override
	public final void end() {
		int finalOffset = this.correctOffset(this.endPosition);
		this.offsetAtt.setOffset(finalOffset, finalOffset);
	}
}